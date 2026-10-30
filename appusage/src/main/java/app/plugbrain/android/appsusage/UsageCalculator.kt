package app.plugbrain.android.appsusage

/**
 * Computes foreground time per package from a chronological list of activity events.
 *
 * Events are tracked per activity: a package is in the foreground while at least one of its
 * activities is resumed. This matters for in-app navigation, where Android reports
 * "A paused → B resumed → A stopped": the late stop of A must not end the session of B.
 *
 * Android does not always report the pause matching a resume (the process is killed, the screen
 * turns off, or the pause comes with another class name, as seen with Samsung Gallery). To keep
 * such a session from running until the end of the window, a session also ends when another
 * package is resumed or when the screen turns off.
 *
 * Events name the activity class, not the instance. When an app opens a new instance of the same
 * activity, Android reports "A paused → A resumed → A stopped", and that last stop belongs to the
 * old instance. A stop after a pause of the same class is therefore that instance going away, and
 * only a stop without a pause before it ends the activity.
 */
object UsageCalculator {
  /**
   * @return foreground time in milliseconds of each package between startTime and endTime.
   */
  fun totalTimePerPackage(
    events: List<AppEvent>,
    startTime: Long,
    endTime: Long,
  ): Map<String, Long> {
    val sessions = mutableMapOf<String, Session>()
    // When the foreground first changed in the window, which ends any session already open at
    // startTime.
    var firstForegroundChange: Long? = null

    fun endAllSessions(timestamp: Long, except: String? = null) {
      sessions.forEach { (packageName, session) ->
        if (packageName != except) session.end(timestamp)
      }
      if (firstForegroundChange == null) firstForegroundChange = timestamp
    }

    events.forEach { event ->
      when (event.type) {
        EventType.SCREEN_OFF -> endAllSessions(event.timestamp)

        EventType.APP_STARTED -> {
          endAllSessions(event.timestamp, except = event.packageName)
          sessions.getOrPut(event.packageName) { Session() }.start(event.className, event.timestamp)
        }

        EventType.APP_PAUSED, EventType.APP_STOPPED -> {
          val session = sessions[event.packageName]
          if (session == null) {
            // The app was already in the foreground when the window started.
            val sessionEnd = minOf(event.timestamp, firstForegroundChange ?: event.timestamp)
            sessions[event.packageName] = Session(totalTime = sessionEnd - startTime).also {
              if (event.type == EventType.APP_PAUSED) it.pause(event.className, event.timestamp)
            }
          } else if (event.type == EventType.APP_PAUSED) {
            session.pause(event.className, event.timestamp)
          } else {
            session.stop(event.className, event.timestamp)
          }
        }
      }
    }
    return sessions.mapValues { (_, session) -> session.end(endTime).totalTime }
  }

  private class Session(var totalTime: Long = 0L) {
    private val resumedActivities = mutableSetOf<String>()

    // Activities paused but not yet stopped: their coming stop must not end a new instance.
    private val pausedActivities = mutableMapOf<String, Int>()
    private var sessionStart = 0L

    fun start(className: String, timestamp: Long) {
      if (resumedActivities.isEmpty()) sessionStart = timestamp
      resumedActivities += className
    }

    fun pause(className: String, timestamp: Long) {
      pausedActivities[className] = (pausedActivities[className] ?: 0) + 1
      remove(className, timestamp)
    }

    fun stop(className: String, timestamp: Long) {
      val paused = pausedActivities[className] ?: 0
      if (paused > 0) {
        pausedActivities[className] = paused - 1
      } else {
        // Android didn't report the pause of this activity.
        remove(className, timestamp)
      }
    }

    private fun remove(className: String, timestamp: Long) {
      if (resumedActivities.remove(className) && resumedActivities.isEmpty()) {
        totalTime += timestamp - sessionStart
      }
    }

    fun end(timestamp: Long): Session {
      if (resumedActivities.isNotEmpty()) totalTime += timestamp - sessionStart
      resumedActivities.clear()
      return this
    }
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
  APP_PAUSED,
  APP_STOPPED,
  SCREEN_OFF,
}
