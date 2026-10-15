package app.plugbrain.android.appsusage

/**
 * Computes foreground time per package from a chronological list of activity events.
 *
 * Events are tracked per activity: a package is in the foreground while at least one of its
 * activities is resumed. This matters for in-app navigation, where Android reports
 * "A paused → B resumed → A stopped": the late stop of A must not end the session of B.
 */
object UsageCalculator {
  /**
   * @return foreground time in milliseconds of each package between startTime and endTime.
   */
  fun totalTimePerPackage(
    events: List<AppEvent>,
    startTime: Long,
    endTime: Long,
  ): Map<String, Long> = events
    .groupBy { it.packageName }
    .mapValues { (_, packageEvents) -> foregroundTime(packageEvents, startTime, endTime) }

  private fun foregroundTime(events: List<AppEvent>, startTime: Long, endTime: Long): Long {
    val resumedActivities = mutableSetOf<String>()
    var sessionStart = startTime
    var totalTime = 0L

    events.forEachIndexed { index, event ->
      when (event.type) {
        EventType.APP_STARTED -> {
          if (resumedActivities.isEmpty()) sessionStart = event.timestamp
          resumedActivities += event.className
        }

        EventType.APP_STOPPED -> {
          if (resumedActivities.remove(event.className)) {
            if (resumedActivities.isEmpty()) totalTime += event.timestamp - sessionStart
          } else if (index == 0) {
            // The app was already in the foreground when the window started.
            totalTime += event.timestamp - startTime
          }
        }
      }
    }
    if (resumedActivities.isNotEmpty()) totalTime += endTime - sessionStart
    return totalTime
  }
}

data class AppEvent(
  val packageName: String,
  val className: String,
  val type: EventType,
  val timestamp: Long,
)

enum class EventType {
  APP_STARTED,
  APP_STOPPED,
}
