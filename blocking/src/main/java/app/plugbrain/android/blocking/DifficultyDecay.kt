package app.plugbrain.android.blocking

private const val MILLIS_PER_MINUTE = 60_000L

// Two block intervals without a challenge cost one difficulty level.
private const val INTERVALS_PER_LEVEL = 2

/** How the difficulty eases off when the user stays away from distractive apps. */
object DifficultyDecay {
  /**
   * @return how many difficulty levels to drop at [now]: one per two block intervals of time away
   * since [lastChallenge] (all of time when there was none). Time away leaves out the
   * [minutesInBlockedApps] spent in blocked apps in that time, so using them never lowers the
   * level (#314). Zero means keep the current level.
   */
  fun levelsToDrop(lastChallenge: Long?, blockIntervalMinutes: Int, now: Long, minutesInBlockedApps: Long): Int {
    val minutesSince = (now - (lastChallenge ?: 0L)) / MILLIS_PER_MINUTE
    val minutesAway = (minutesSince - minutesInBlockedApps).coerceAtLeast(0)
    return (minutesAway / (blockIntervalMinutes * INTERVALS_PER_LEVEL)).toInt()
  }
}
