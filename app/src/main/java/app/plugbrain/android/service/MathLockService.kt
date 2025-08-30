package app.plugbrain.android.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.plugbrain.android.R
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.datastore.decrementProgressiveDifficulty
import app.plugbrain.android.datastore.getBlockAppsToggle
import app.plugbrain.android.datastore.getTimestamps
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.model.Timestamps
import app.plugbrain.android.datastore.model.UserSettings
import app.plugbrain.android.datastore.setLastBlockTime
import app.plugbrain.android.datastore.setLastUsageTime
import app.plugbrain.android.datastore.updateBlockAppsToggle
import app.plugbrain.android.ui.challenges.ChallengeActivity
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

const val TAG = "MathLockService"

private const val ONE_MINUTE = 60_000L
private const val ONE_HOUR = 60 * ONE_MINUTE

class MathLockService : AccessibilityService() {
  private val dataStore: DataStore<Preferences> by inject()
  private val appsUsageStats: AppsUsageStats by inject()
  private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

  // TODO Refactor using TimeStats
  private var userSettings = UserSettings()
  private var timestamps = Timestamps()
  private var isBlocked: Boolean? = null

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
      if (eventPackageName in userSettings.distractiveApps) {
        if (isBlocked == true) {
          launchChallenge()
        } else {
          serviceScope.launch {
            dataStore.setLastUsageTime(System.currentTimeMillis())
          }
          checkAppUsage()
        }
      }
      if ((eventPackageName in resources.getStringArray(R.array.calculator_packages) || "calc" in eventPackageName) && (isBlocked == true)) {
        Toast.makeText(this, "Please, don't use the calculator!", Toast.LENGTH_LONG).show()
      }
    }
  }

  private fun launchChallenge() {
    Timber.tag(TAG).i("Launch Challenge ...")
    serviceScope.launch {
      decreaseDifficulty()
    }
    if (ChallengeActivity.isInForeground) return

    val intent = Intent(this@MathLockService, ChallengeActivity::class.java)
    intent.addFlags(
      Intent.FLAG_ACTIVITY_NEW_TASK or
        Intent.FLAG_ACTIVITY_CLEAR_TOP or
        Intent.FLAG_ACTIVITY_CLEAR_TASK or
        Intent.FLAG_ACTIVITY_SINGLE_TOP,
    )
    startActivity(intent)
  }

  suspend fun decreaseDifficulty() {
    val durationSinceLastChallenge = (
      System.currentTimeMillis() - (timestamps.lastChallenge ?: 0)
      ).milliseconds.inWholeMinutes.toInt()
    if (durationSinceLastChallenge >= userSettings.blockInterval * 2) {
      val nbDecLevels = durationSinceLastChallenge / (userSettings.blockInterval * 2)
      dataStore.decrementProgressiveDifficulty(nbDecLevels)
    }
  }

  override fun onInterrupt() {}

  private fun checkAppUsage() {
    if (isBlocked == true) return
    val blockedAppsUsageTime = appsUsageStats.getTotalAppsUsageDuration(
      startTime = timestamps.lastBlock ?: (System.currentTimeMillis() - ONE_HOUR),
      endTime = System.currentTimeMillis(),
      filterPackages = userSettings.distractiveApps,
    )
    if (blockedAppsUsageTime >= userSettings.blockInterval) {
      serviceScope.launch {
        dataStore.updateBlockAppsToggle(true)
        timestamps = timestamps.copy(lastBlock = System.currentTimeMillis())
        dataStore.setLastBlockTime(System.currentTimeMillis())
      }
      launchChallenge()
    } else {
      scheduleAppUsageCheck(userSettings.blockInterval - blockedAppsUsageTime)
    }
  }

  private fun scheduleAppUsageCheck(delayCheckBy: Long) {
    serviceScope.launch {
      val targetTime = System.currentTimeMillis() + delayCheckBy * 60 * 1000L
      while (isActive && System.currentTimeMillis() < targetTime) {
        delay(1000) // delay(delayCheckBy ...) was not working for recent Android versions!
        // TODO find a cleaner solution
      }
      checkAppUsage()
    }
  }
}
