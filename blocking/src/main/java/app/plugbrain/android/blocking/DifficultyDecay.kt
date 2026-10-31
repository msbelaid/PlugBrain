package app.plugbrain.android.blocking

private const val MILLIS_PER_MINUTE = 60_000L

// Two block intervals without a challenge cost one difficulty level.
private const val INTERVALS_PER_LEVEL = 2

/** How the difficulty eases off when the user stays away from distractive apps. */
object DifficultyDecay {
  /**
   * @return how many difficulty levels to drop at [now]: one per two block intervals since
   * [lastChallenge] (all of time when there was none). Zero means keep the current level.
   */
  fun levelsToDrop(lastChallenge: Long?, blockIntervalMinutes: Int, now: Long): Int {
    val minutesSince = (now - (lastChallenge ?: 0L)) / MILLIS_PER_MINUTE
    return (minutesSince / (blockIntervalMinutes * INTERVALS_PER_LEVEL)).toInt()
  }
}
