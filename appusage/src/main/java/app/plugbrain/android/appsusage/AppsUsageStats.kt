package app.plugbrain.android.appsusage

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.USAGE_STATS_SERVICE
import app.plugbrain.android.appsusage.model.ActivitySpan
import timber.log.Timber

private const val TAG = "AppsUsageStats"
private const val ONE_MINUTE = 60_000L

private val STARTED_EVENTS = setOf(UsageEvents.Event.ACTIVITY_RESUMED)
private val STOPPED_EVENTS = setOf(UsageEvents.Event.ACTIVITY_PAUSED, UsageEvents.Event.ACTIVITY_STOPPED)

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
    val events = usageStatsManager.queryEvents(startTime, endTime).toAppEvents(filterPackages)
    val totalTimeInMinutes =
      UsageCalculator.totalTimePerPackage(events, startTime, endTime).values.sum() / ONE_MINUTE
    Timber.tag(TAG).i("   Total Time spent on blocked packages: $totalTimeInMinutes")
    return totalTimeInMinutes
  }

  /**
   * @return foreground time(in milliseconds) of each app in filterPackages between startTime
   * and endTime. Apps that were not used are missing from the map.
   */
  fun getUsagePerPackage(
    startTime: Long,
    endTime: Long,
    filterPackages: Set<String>,
  ): Map<String, Long> {
    val usageStatsManager = context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val events = usageStatsManager.queryEvents(startTime, endTime).toAppEvents(filterPackages)
    return UsageCalculator.totalTimePerPackage(events, startTime, endTime)
  }

  /**
   * @return what the user was doing between startTime and endTime: away from the phone, on one
   * of distractiveApps, or on another app, as consecutive spans.
   */
  fun getActivityTimeline(
    startTime: Long,
    endTime: Long,
    distractiveApps: Set<String>,
  ): List<ActivitySpan> {
    val usageStatsManager = context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val events = usageStatsManager.queryEvents(startTime, endTime).toPhoneEvents()
    return ActivityTimelineCalculator.timeline(events, distractiveApps, startTime, endTime)
  }

  private fun UsageEvents.toPhoneEvents(): List<PhoneEvent> {
    val event = UsageEvents.Event()
    val phoneEvents = mutableListOf<PhoneEvent>()

    while (hasNextEvent()) {
      getNextEvent(event)
      phoneEvents += when (event.eventType) {
        UsageEvents.Event.SCREEN_INTERACTIVE -> PhoneEvent.ScreenOn(event.timeStamp)
        UsageEvents.Event.SCREEN_NON_INTERACTIVE -> PhoneEvent.ScreenOff(event.timeStamp)
        in STARTED_EVENTS -> event.toAppPhoneEvent(EventType.APP_STARTED)
        in STOPPED_EVENTS -> event.toAppPhoneEvent(EventType.APP_STOPPED)
        else -> continue
      }
    }
    return phoneEvents
  }

  private fun UsageEvents.Event.toAppPhoneEvent(type: EventType) =
    PhoneEvent.App(AppEvent(packageName, className.orEmpty(), type, timeStamp))

  private fun UsageEvents.toAppEvents(filterPackages: Set<String>): List<AppEvent> {
    val event = UsageEvents.Event()
    val appEvents = mutableListOf<AppEvent>()

    while (hasNextEvent()) {
      getNextEvent(event)
      if (event.packageName !in filterPackages) continue

      val type = when (event.eventType) {
        in STARTED_EVENTS -> EventType.APP_STARTED
        in STOPPED_EVENTS -> EventType.APP_STOPPED
        else -> continue
      }
      appEvents += AppEvent(event.packageName, event.className.orEmpty(), type, event.timeStamp)
    }
    return appEvents
  }
}
