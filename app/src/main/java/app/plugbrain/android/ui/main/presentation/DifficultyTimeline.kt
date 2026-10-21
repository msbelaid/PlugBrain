package app.plugbrain.android.ui.main.presentation

import app.plugbrain.android.appsusage.model.ActivitySpan
import app.plugbrain.android.appsusage.model.PhoneActivity
import app.plugbrain.android.datastore.model.DifficultyChange
import app.plugbrain.android.datastore.projectDifficulty
import java.util.Calendar
import java.util.TimeZone

const val TIMELINE_DURATION = 24 * 60 * 60_000L
private const val ONE_HOUR = 60 * 60_000L

/** Hours that get a label on the time axis, and how close to either end a label may sit. */
private const val TICK_EVERY_HOURS = 6
private const val TICK_MARGIN = 2 * ONE_HOUR

/**
 * The challenge level from [start] to [end], as steps that each hold until the next one, and
 * what the user was doing meanwhile.
 */
data class DifficultyTimeline(
  val start: Long,
  val end: Long,
  val levels: List<DifficultyChange>,
  val activity: List<ActivitySpan>,
) {
  val currentLevel: Int? get() = levels.lastOrNull()?.level

  val lowestLevel: Int? get() = levels.minOfOrNull { it.level }

  val highestLevel: Int? get() = levels.maxOfOrNull { it.level }

  fun millisSpent(on: PhoneActivity): Long = activity.filter { it.activity == on }.sumOf { it.end - it.start }
}

/**
 * Builds the last [TIMELINE_DURATION] before [now]. Without any logged change yet, as right after
 * an update, the stored level and the time of the last challenge stand in for one.
 */
internal fun difficultyTimeline(
  history: List<DifficultyChange>,
  storedLevel: Int,
  lastChallenge: Long?,
  activity: List<ActivitySpan>,
  now: Long,
  blockInterval: Int,
  minDifficulty: Int,
): DifficultyTimeline {
  val start = now - TIMELINE_DURATION
  val changes = history.ifEmpty { listOf(DifficultyChange(lastChallenge ?: start, storedLevel)) }
  return DifficultyTimeline(
    start = start,
    end = now,
    levels = projectDifficulty(changes, start, now, blockInterval, minDifficulty),
    activity = activity,
  )
}

/** Every [TICK_EVERY_HOURS]th hour of the day between [start] and [end], away from both ends. */
fun timelineHourTicks(start: Long, end: Long, timeZone: TimeZone = TimeZone.getDefault()): List<Long> {
  val calendar = Calendar.getInstance(timeZone).apply {
    timeInMillis = start + TICK_MARGIN
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
  }
  val ticks = mutableListOf<Long>()
  while (calendar.timeInMillis <= end - TICK_MARGIN) {
    if (calendar.timeInMillis >= start + TICK_MARGIN && calendar.get(Calendar.HOUR_OF_DAY) % TICK_EVERY_HOURS == 0) {
      ticks += calendar.timeInMillis
    }
    calendar.add(Calendar.HOUR_OF_DAY, 1)
  }
  return ticks
}
