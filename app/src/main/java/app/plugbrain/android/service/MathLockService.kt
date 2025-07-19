package app.plugbrain.android.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import app.plugbrain.android.R
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.datastore.DataStoreManager
import app.plugbrain.android.ui.challenges.ChallengeActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

const val TAG = "MathLockService"

private const val ONE_MINUTE = 60_000L
private const val ONE_HOUR = 60 * ONE_MINUTE

class MathLockService : AccessibilityService() {
  private val dataStoreManager: DataStoreManager by inject()
  private val appsUsageStats: AppsUsageStats by inject()
  private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

  private var blockInterval = 1
  private var lastBlockTime: Long? = null

  // TODO Refactor using TimeStats
  private var blockedPackages: Set<String> = emptySet()
  private var lastUsageTime: Long? = null
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
        dataStoreManager.getBlockedApps(),
        dataStoreManager.getBlockInterval(),
        dataStoreManager.getLastBlockTime(),
        dataStoreManager.getLastUsageTime(),
        dataStoreManager.getBlockAppsToggle(),
      ) { blockedPackages, interval, lastTime, usageTime, toggle ->
        State(blockedPackages, interval, lastTime, usageTime, toggle)
      }.collect {
        blockedPackages = it.blockedPackages
        blockInterval = it.interval
        lastBlockTime = it.lastTime
        lastUsageTime = it.usageTime
        isBlocked = it.toggle
      }
    }
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      val eventPackageName = event.packageName?.toString() ?: return
      Timber.tag(TAG).i("Event: $eventPackageName state changed")
      if (eventPackageName in blockedPackages) {
        if (isBlocked == true) {
          launchChallenge()
        } else {
          serviceScope.launch {
            dataStoreManager.updateLastUsageTime(System.currentTimeMillis())
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
    Timber.tag(TAG).i("Decrease Difficulty if applicable ...")
    val it = dataStoreManager.getTimeStats().first()
    val durationSinceLastChallenge = (
      System.currentTimeMillis() - (it.lastChallengeGenerateTime ?: 0)
      ).milliseconds.inWholeMinutes.toInt()
    Timber.tag(TAG).e("Duration since last challenge:%s", durationSinceLastChallenge.toString())
    if (durationSinceLastChallenge >= it.blockInterval * 2) {
      val nbDecLevels = durationSinceLastChallenge / (it.blockInterval * 2)
      dataStoreManager.decrementProgressiveDifficulty(nbDecLevels)
    }
  }

  override fun onInterrupt() {}

  private fun checkAppUsage() {
    Timber.tag(TAG).i("* Check blocked app usage started")
    Timber.tag(TAG).i("   Interval: $blockInterval min")
    Timber.tag(TAG).i("   Blocked packages: $blockedPackages")
    Timber.tag(TAG).i("   Blocked?: $isBlocked")
    Timber.tag(TAG).i(
      "   Last check : ${
        SimpleDateFormat(
          "yyyy-MM-dd HH:mm:ss",
          Locale.getDefault(),
        ).format(Date(lastBlockTime ?: 0))
      }",
    )
    if (isBlocked == true) return
    val blockedAppsUsageTime = appsUsageStats.getTotalAppsUsageDuration(
      startTime = lastBlockTime ?: (System.currentTimeMillis() - ONE_HOUR),
      endTime = System.currentTimeMillis(),
      filterPackages = blockedPackages,
    )
    if (blockedAppsUsageTime >= blockInterval) {
      serviceScope.launch {
        dataStoreManager.updateBlockAppsToggle(true)
        lastBlockTime = System.currentTimeMillis()
        dataStoreManager.updateLastBlockTime(System.currentTimeMillis())
      }
      launchChallenge()
    } else {
      scheduleAppUsageCheck(blockInterval - blockedAppsUsageTime)
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

  data class State(
    val blockedPackages: Set<String>,
    val interval: Int,
    val lastTime: Long?,
    val usageTime: Long?,
    val toggle: Boolean?,
  )
}
