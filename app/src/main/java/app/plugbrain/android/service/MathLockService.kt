package app.plugbrain.android.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.plugbrain.android.R
import app.plugbrain.android.datastore.getBlockAppsToggle
import app.plugbrain.android.datastore.getTimestamps
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.model.Timestamps
import app.plugbrain.android.datastore.model.UserSettings
import app.plugbrain.android.datastore.setLastBlockTime
import app.plugbrain.android.datastore.setLastUsageTime
import app.plugbrain.android.datastore.updateBlockAppsToggle
import app.plugbrain.android.ui.challenges.ChallengeActivity
import app.plugbrain.android.util.Clock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

const val TAG = "MathLockService"

private const val ONE_MINUTE = 60_000L
private const val USAGE_CHECK_STEP = 10_000L
private const val SYSTEM_UI_PACKAGE = "com.android.systemui"
private const val CHALLENGE_RECHECK_DELAY = 700L

class MathLockService : AccessibilityService() {
  private val dataStore: DataStore<Preferences> by inject()
  private val usageLimit: UsageLimit by inject()
  private val clock: Clock by inject()
  private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

  // TODO Refactor using TimeStats
  private var userSettings = UserSettings()
  private var timestamps = Timestamps()
  private var isBlocked: Boolean? = null

  // Only one pending usage check at a time, otherwise every window event of a
  // distractive app would start a new polling loop.
  private var usageCheckJob: Job? = null

  private val appOpenDetector = AppOpenDetector { ignoredPackages() }

  // When the last open challenge was shown. Until it's solved, every event of a distractive app
  // brings it back, so leaving it through recents doesn't skip it.
  private var openChallengeShownAt: Long? = null

  private var challengeRecheckJob: Job? = null

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Timber.tag(TAG).i("onStart")
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onServiceConnected() {
    Timber.tag(TAG).i("onServiceConnected")
    getData()
    super.onServiceConnected()
  }

  private fun getData() {
    serviceScope.launch {
      combine(
        dataStore.getUserSettings(),
        dataStore.getTimestamps(),
        dataStore.getBlockAppsToggle(),
      ) { userSettings, timestamps, toggle ->
        Triple(userSettings, timestamps, toggle)
      }.collect { (settings, ts, toggle) ->
        userSettings = settings
        timestamps = ts
        isBlocked = toggle
      }
    }
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      val eventPackageName = event.packageName?.toString() ?: return
      Timber.tag(TAG).i("Event: $eventPackageName state changed")
      val isAppOpening = appOpenDetector.isAppOpening(eventPackageName, userSettings.distractiveApps)
      if (eventPackageName in userSettings.distractiveApps) {
        if (isBlocked == true) {
          launchChallenge()
        } else if ((isAppOpening || isOpenChallengeUnsolved()) && shouldChallengeOnOpen()) {
          openChallengeShownAt = clock.now()
          launchChallenge()
        } else {
          serviceScope.launch {
            dataStore.setLastUsageTime(clock.now())
          }
          checkAppUsage()
        }
      } else if (isCalculator(eventPackageName) && isBlocked == true) {
        // Showing a toast alone left the calculator usable; cover it with the challenge.
        Toast.makeText(this, R.string.calculator_warning, Toast.LENGTH_LONG).show()
        launchChallenge()
      }
    }
  }

  private fun shouldChallengeOnOpen(): Boolean = userSettings.challengeOnAppOpen &&
    isOpenChallengeDue(timestamps.lastUnblock, userSettings.blockInterval, clock.now())

  private fun isOpenChallengeUnsolved(): Boolean {
    val shownAt = openChallengeShownAt ?: return false
    return (timestamps.lastUnblock ?: 0L) < shownAt
  }

  private fun ignoredPackages(): Set<String> {
    val keyboardPackage = Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
      ?.substringBefore('/')
    return setOfNotNull(packageName, SYSTEM_UI_PACKAGE, keyboardPackage)
  }

  private fun isCalculator(packageName: String): Boolean =
    packageName in resources.getStringArray(R.array.calculator_packages) || "calc" in packageName

  private fun isChallengeDue(): Boolean =
    isBlocked == true || (isOpenChallengeUnsolved() && shouldChallengeOnOpen())

  private fun launchChallenge() {
    Timber.tag(TAG).i("Launch Challenge ...")
    if (ChallengeActivity.isInForeground) {
      // An app that is still starting up can bring itself back over a challenge that just opened,
      // before the challenge screen is paused. Look again once that settles.
      scheduleChallengeRecheck()
      return
    }

    val intent = Intent(this@MathLockService, ChallengeActivity::class.java)
    intent.addFlags(
      Intent.FLAG_ACTIVITY_NEW_TASK or
        Intent.FLAG_ACTIVITY_CLEAR_TOP or
        Intent.FLAG_ACTIVITY_CLEAR_TASK or
        Intent.FLAG_ACTIVITY_SINGLE_TOP,
    )
    startActivity(intent)
  }

  private fun scheduleChallengeRecheck() {
    challengeRecheckJob?.cancel()
    challengeRecheckJob = serviceScope.launch(Dispatchers.Main) {
      delay(CHALLENGE_RECHECK_DELAY)
      // Still showing means the challenge won; only a blocked app back on top needs it again.
      if (ChallengeActivity.isInForeground) return@launch
      val foreground = appOpenDetector.foregroundPackage ?: return@launch
      if (foreground in userSettings.distractiveApps && isChallengeDue()) {
        launchChallenge()
      }
    }
  }

  override fun onInterrupt() {
    // Nothing to interrupt: the service gives no spoken or haptic feedback.
  }

  private fun checkAppUsage() {
    if (isBlocked == true) return
    val minutesLeft = usageLimit.minutesLeft(userSettings, timestamps.lastBlock)
    if (minutesLeft <= 0) {
      val now = clock.now()
      serviceScope.launch {
        dataStore.updateBlockAppsToggle(true)
        timestamps = timestamps.copy(lastBlock = now)
        dataStore.setLastBlockTime(now)
      }
      launchChallenge()
    } else {
      scheduleAppUsageCheck(minutesLeft)
    }
  }

  private fun scheduleAppUsageCheck(delayCheckBy: Long) {
    usageCheckJob?.cancel()
    // Runs on Main so usageCheckJob is only ever touched from the main thread.
    usageCheckJob = serviceScope.launch(Dispatchers.Main) {
      val targetTime = System.currentTimeMillis() + delayCheckBy * ONE_MINUTE
      // Sleep in bounded steps against the wall clock: a single long delay() was not
      // reliable on recent Android versions, and 1 s steps woke the CPU needlessly.
      while (isActive) {
        val remaining = targetTime - System.currentTimeMillis()
        if (remaining <= 0) break
        delay(minOf(remaining, USAGE_CHECK_STEP))
      }
      checkAppUsage()
    }
  }
}
