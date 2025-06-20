package app.plugbrain.android.appsusage

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.USAGE_STATS_SERVICE
import app.plugbrain.android.service.TAG
import timber.log.Timber

private const val ONE_MINUTE = 60_000L

class AppsUsageStats(
  private val context: Context,
) {
  /**
   * @return usage time(in minutes) of all app in filterPackages between startTime and endTime.
   */
  fun getTotalAppsUsageDuration(
    startTime: Long,
    endTime: Long,
    filterPackages: Set<String>,
  ): Long {
    val usageStatsManager = context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val eventsPerPackage =
      usageStatsManager.queryEvents(startTime, endTime).groupTimeByPackage(filterPackages)
    val totalTimeInMinutes =
      eventsPerPackage.totalTimePerPackage(startTime, endTime).values.sum() / ONE_MINUTE
    Timber.tag(TAG).i("   Total Time spent on blocked packages: $totalTimeInMinutes")
    return totalTimeInMinutes
  }

  private fun UsageEvents.groupTimeByPackage(filterPackage: Set<String>): MutableMap<String, MutableList<AppEvent>> {
    val event = UsageEvents.Event()
    val eventsPerApp: MutableMap<String, MutableList<AppEvent>> = HashMap()

    while (this.hasNextEvent()) {
      this.getNextEvent(event)

      if (event.packageName in filterPackage &&
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

  private fun MutableMap<String, MutableList<AppEvent>>.totalTimePerPackage(startTime: Long, endTime: Long): Map<String, Long> {
    val timePerApp = this.mapValues { app ->
      var start = startTime
      var inForeground = (app.value.first().type == EventType.APP_STOPPED)
      var totalTime = 0L
      app.value.add(AppEvent(EventType.APP_STOPPED, endTime))
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
      totalTime
    }
    return timePerApp
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
