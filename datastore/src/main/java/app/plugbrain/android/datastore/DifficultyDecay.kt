package app.plugbrain.android.datastore

import app.plugbrain.android.datastore.model.DifficultyChange

private const val ONE_MINUTE = 60_000L

/** The challenge level drops by one for every two block intervals without a challenge. */
fun difficultyDecaySteps(minutesSinceLastChallenge: Long, blockInterval: Int): Int =
  if (blockInterval <= 0 || minutesSinceLastChallenge <= 0) 0 else (minutesSinceLastChallenge / (blockInterval * 2)).toInt()

/** The level a challenge would have at [time], when the level was last set to [change]. */
fun decayedDifficulty(change: DifficultyChange, time: Long, blockInterval: Int, minDifficulty: Int): Int {
  val steps = difficultyDecaySteps((time - change.timestamp) / ONE_MINUTE, blockInterval)
  // Same rule as decrementProgressiveDifficulty: once it drops, it stops at the minimum.
  return if (steps > 0) (change.level - steps).coerceAtLeast(minDifficulty) else change.level
}

/**
 * The challenge level between [startTime] and [endTime], as steps: each level holds from its
 * timestamp until the next one. The level is only stored when it changes, so the drops in
 * between are worked out here with the same rule the next challenge would use.
 *
 * The steps start at [startTime] when the history knows the level then, otherwise at the first
 * change after it.
 */
fun projectDifficulty(
  history: List<DifficultyChange>,
  startTime: Long,
  endTime: Long,
  blockInterval: Int,
  minDifficulty: Int,
): List<DifficultyChange> {
  val sorted = history.sortedBy { it.timestamp }.filter { it.timestamp <= endTime }
  val firstShown = (sorted.indexOfLast { it.timestamp <= startTime }).coerceAtLeast(0)
  val steps = mutableListOf<DifficultyChange>()

  // Keeps one step per moment and only the moments where the level changes.
  fun add(time: Long, level: Int) {
    val last = steps.lastOrNull()
    when {
      last == null -> steps += DifficultyChange(time, level)
      last.timestamp == time -> {
        steps.removeAt(steps.lastIndex)
        if (steps.lastOrNull()?.level != level) steps += DifficultyChange(time, level)
      }
      last.level != level -> steps += DifficultyChange(time, level)
    }
  }

  for (index in firstShown until sorted.size) {
    val change = sorted[index]
    val until = sorted.getOrNull(index + 1)?.timestamp ?: endTime
    val from = maxOf(change.timestamp, startTime)
    add(from, decayedDifficulty(change, from, blockInterval, minDifficulty))
    if (blockInterval <= 0) continue
    // Walk the moments the level drops by one, until it reaches the minimum or the next change.
    val stepMillis = blockInterval * 2 * ONE_MINUTE
    var time = change.timestamp + ((from - change.timestamp) / stepMillis + 1) * stepMillis
    while (time < until) {
      val level = decayedDifficulty(change, time, blockInterval, minDifficulty)
      add(time, level)
      if (level <= minDifficulty) break
      time += stepMillis
    }
  }
  return steps
}
