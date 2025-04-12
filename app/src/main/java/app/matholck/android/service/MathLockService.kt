package app.matholck.android.service

import android.accessibilityservice.AccessibilityService
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import app.matholck.android.ui.challenges.ChallengeActivity
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MathLockService : AccessibilityService() {
  private val dataStoreManager: DataStoreManager by inject()
  private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

  private var blockedPackages: Set<String> = emptySet()
  private var blockInterval = 5
  private var lastBlockTime: Long? = null

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Log.e("MathLockService", "service started")
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onServiceConnected() {
    Log.e("MathLockService", "service connected")
    serviceScope.launch {
      dataStoreManager.getBlockedApps.collect { blockedPackages = it }
      dataStoreManager.getBlockInterval.collect { blockInterval = it }
      dataStoreManager.getLastBlockTime.collect { lastBlockTime = it }
    }
    startRepeatingUsageCheck()
    super.onServiceConnected()
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      val packageName = event.packageName?.toString() ?: return
      Log.e("MathLockService", "Current Package $packageName")
      Log.e("MathLockService", "BlockedPackages $blockedPackages")
      if (packageName in blockedPackages) checkAppUsage()
    }
  }

  private fun launchMathChallenge() {
    Log.e("MathLockService", "launchMathChallenge")
    val intent = Intent(this, ChallengeActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
  }

  override fun onInterrupt() {}

  fun getBlockedAppUsageDuration(): Long {
    val usageStatsManager =
      this.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val endTime = System.currentTimeMillis()
    val startTime = lastBlockTime ?: (endTime - 2 * 60 * 60 * 1000)
    val eventsPerPackage = usageStatsManager.queryEvents(startTime, endTime).groupTimeByPackage()
    Log.e("MathLockService", eventsPerPackage.toString())

    return eventsPerPackage.totalTime()
  }

  private fun UsageEvents.groupTimeByPackage(): MutableMap<String, MutableList<AppEvent>> {
    val event = UsageEvents.Event()
    val eventsPerApp: MutableMap<String, MutableList<AppEvent>> = HashMap()

    while (this.hasNextEvent()) {
      this.getNextEvent(event)

      if (event.packageName in blockedPackages &&
        event.eventType in setOf(
          UsageEvents.Event.ACTIVITY_RESUMED,
          UsageEvents.Event.ACTIVITY_STOPPED,
          UsageEvents.Event.ACTIVITY_PAUSED,
          UsageEvents.Event.MOVE_TO_FOREGROUND,
          UsageEvents.Event.MOVE_TO_BACKGROUND,
        )
      ) {
        val appEvent = AppEvent(
          when (event.eventType) {
            UsageEvents.Event.ACTIVITY_RESUMED, UsageEvents.Event.MOVE_TO_FOREGROUND -> EventType.APP_STARTED
            else -> EventType.APP_STOPPED
          },
          event.timeStamp,
        )
        if (eventsPerApp[event.packageName] == null) {
          eventsPerApp[event.packageName] = mutableListOf(appEvent)
        } else {
          eventsPerApp[event.packageName]?.add(appEvent)
        }
      }
    }
    return eventsPerApp
  }

  private fun MutableMap<String, MutableList<AppEvent>>.totalTime(): Long {
    val now = System.currentTimeMillis()
    val timePerApp = this.map { app ->
      var start = lastBlockTime ?: (now - 2 * 60 * 60 * 1000)
      var inForeground = (app.value.first().type == EventType.APP_STOPPED)
      var totalTime = 0L
      app.value.add(AppEvent(EventType.APP_STOPPED, now))
      app.value.forEach { event ->
        when (event.type) {
          EventType.APP_STARTED -> {
            start = event.timestamp
            inForeground = true
          }

          EventType.APP_STOPPED -> {
            if (inForeground) {
              totalTime += (event.timestamp - start)
              inForeground = false
            }
          }
        }
      }
      app.key to totalTime
    }
    Log.e("MathLockService", timePerApp.toString())

    return timePerApp.sumOf { it.second } / 60000L
  }

  private fun startRepeatingUsageCheck() {
    serviceScope.launch {
      while (isActive) {
        checkAppUsage()
        delay(blockInterval * 60_000L + 1000)
      }
    }
  }

  private fun checkAppUsage() {
    Log.e("MathLockService", "using events for  ${getBlockedAppUsageDuration()} mins")

    if (getBlockedAppUsageDuration() >= blockInterval) {
      serviceScope.launch {
        lastBlockTime = System.currentTimeMillis()
        // TODO not here, when challenge resolved
        dataStoreManager.updateLastBlockTime(System.currentTimeMillis())
      }
      launchMathChallenge()
    }
  }

  data class AppEvent(
    val type: EventType,
    val timestamp: Long,
  )

  enum class EventType {
    APP_STARTED,
    APP_STOPPED,
  }
}
