package app.plugbrain.android.appsusage

import app.plugbrain.android.appsusage.model.ActivitySpan
import app.plugbrain.android.appsusage.model.PhoneActivity

/** Spans shorter than this, such as a glance at a notification, join the span before them. */
internal const val MIN_SPAN_MILLIS = 60_000L

/**
 * Turns a chronological list of screen and activity events into what the user was doing:
 * away while the screen was off, otherwise on a distracting app or another app.
 */
object ActivityTimelineCalculator {
  fun timeline(
    events: List<PhoneEvent>,
    distractiveApps: Set<String>,
    startTime: Long,
    endTime: Long,
  ): List<ActivitySpan> {
    if (endTime <= startTime) return emptyList()
    var screenOn = initialScreenOn(events)
    // As in UsageCalculator, an app stays in the foreground while one of its activities is resumed.
    // An app whose first event is a stop was already in the foreground when the window started.
    val firstAppEvent = events.firstNotNullOfOrNull { (it as? PhoneEvent.App)?.event }
    val alreadyOpen = firstAppEvent?.takeIf { it.type == EventType.APP_STOPPED }
    var foregroundApp: String? = alreadyOpen?.packageName
    val resumedActivities = listOfNotNull(alreadyOpen?.className).toMutableSet()

    fun current() = when {
      !screenOn -> PhoneActivity.Away
      foregroundApp in distractiveApps -> PhoneActivity.DistractingApp
      else -> PhoneActivity.OtherApp
    }

    val spans = mutableListOf<ActivitySpan>()
    var spanStart = startTime
    var activity = current()
    for (event in events) {
      when (event) {
        is PhoneEvent.ScreenOn -> screenOn = true
        is PhoneEvent.ScreenOff -> screenOn = false
        is PhoneEvent.App -> when (event.event.type) {
          EventType.APP_STARTED -> {
            if (event.event.packageName != foregroundApp) {
              foregroundApp = event.event.packageName
              resumedActivities.clear()
            }
            resumedActivities += event.event.className
          }
          EventType.APP_STOPPED -> if (event.event.packageName == foregroundApp) {
            resumedActivities -= event.event.className
            if (resumedActivities.isEmpty()) foregroundApp = null
          }
        }
      }
      val next = current()
      if (next != activity) {
        val time = event.timestamp.coerceIn(startTime, endTime)
        spans += ActivitySpan(spanStart, time, activity)
        spanStart = time
        activity = next
      }
    }
    spans += ActivitySpan(spanStart, endTime, activity)
    return merge(spans)
  }

  /**
   * The screen state before the first event: the opposite of the first screen change. Without
   * any screen change, the screen stayed on if an app was used and off otherwise.
   */
  private fun initialScreenOn(events: List<PhoneEvent>): Boolean =
    when (events.firstOrNull { it is PhoneEvent.ScreenOn || it is PhoneEvent.ScreenOff }) {
      is PhoneEvent.ScreenOn -> false
      is PhoneEvent.ScreenOff -> true
      else -> events.any { it is PhoneEvent.App }
    }

  private fun merge(spans: List<ActivitySpan>): List<ActivitySpan> {
    val merged = mutableListOf<ActivitySpan>()
    for (span in spans) {
      if (span.end <= span.start) continue
      val last = merged.lastOrNull()
      merged += when {
        last == null -> span
        last.activity == span.activity || span.end - span.start < MIN_SPAN_MILLIS -> {
          merged.removeAt(merged.lastIndex)
          last.copy(end = span.end)
        }
        else -> span
      }
    }
    return merged
  }
}

sealed interface PhoneEvent {
  val timestamp: Long

  data class ScreenOn(override val timestamp: Long) : PhoneEvent

  data class ScreenOff(override val timestamp: Long) : PhoneEvent

  data class App(val event: AppEvent) : PhoneEvent {
    override val timestamp: Long get() = event.timestamp
  }
}
