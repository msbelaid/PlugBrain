package app.plugbrain.android.appsusage

import android.annotation.SuppressLint
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.USAGE_STATS_SERVICE
import timber.log.Timber

private const val TAG = "AppsUsageStats"
private const val ONE_MINUTE = 60_000L

private val STARTED_EVENTS = setOf(UsageEvents.Event.ACTIVITY_RESUMED)
private val STOPPED_EVENTS = setOf(UsageEvents.Event.ACTIVITY_PAUSED, UsageEvents.Event.ACTIVITY_STOPPED)

// DEVICE_SHUTDOWN is a constant from API 29; older devices never report it.
@SuppressLint("InlinedApi")
private val SCREEN_OFF_EVENTS = setOf(UsageEvents.Event.SCREEN_NON_INTERACTIVE, UsageEvents.Event.DEVICE_SHUTDOWN)

/** Reads usage from Android's [UsageStatsManager], which needs the usage access permission. */
class SystemAppsUsageStats(
  private val context: Context,
) : AppsUsageStats {
  override fun getTotalAppsUsageDuration(
    startTime: Long,
    endTime: Long,
    filterPackages: Set<String>,
  ): Long {
    val usageStatsManager = context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val events = usageStatsManager.queryEvents(startTime, endTime).toAppEvents()
    val totalTimeInMinutes = UsageCalculator.totalTimePerPackage(events, startTime, endTime)
      .filterKeys { it in filterPackages }
      .values.sum() / ONE_MINUTE
    Timber.tag(TAG).i("   Total Time spent on blocked packages: $totalTimeInMinutes")
    return totalTimeInMinutes
  }

  override fun getUsagePerPackage(
    startTime: Long,
    endTime: Long,
    filterPackages: Set<String>,
  ): Map<String, Long> {
    val usageStatsManager = context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val events = usageStatsManager.queryEvents(startTime, endTime).toAppEvents()
    return UsageCalculator.totalTimePerPackage(events, startTime, endTime)
      .filterKeys { it in filterPackages }
  }

  // Events of every package are kept: another app coming to the foreground ends the session of
  // the previous one, even when Android did not report its pause.
  private fun UsageEvents.toAppEvents(): List<AppEvent> {
    val event = UsageEvents.Event()
    val appEvents = mutableListOf<AppEvent>()

    while (hasNextEvent()) {
      getNextEvent(event)
      val type = appEventType(event.eventType)
      Timber.tag(TAG).i("DEBUG event ${event.timeStamp} ${event.eventType} $type ${event.packageName}/${event.className}")
      if (type == null) continue
      appEvents += AppEvent(event.packageName.orEmpty(), event.className.orEmpty(), type, event.timeStamp)
    }
    return appEvents
  }
}

/** What an Android usage event type means for foreground time; null for the events that are ignored. */
internal fun appEventType(usageEventType: Int): EventType? = when (usageEventType) {
  in STARTED_EVENTS -> EventType.APP_STARTED
  in STOPPED_EVENTS -> EventType.APP_STOPPED
  in SCREEN_OFF_EVENTS -> EventType.SCREEN_OFF
  else -> null
}
