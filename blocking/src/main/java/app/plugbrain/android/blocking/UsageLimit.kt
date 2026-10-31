package app.plugbrain.android.blocking

private const val ONE_HOUR = 60 * 60_000L

/** How much of the block interval is left before distractive apps get blocked. */
class UsageLimit(private val usageSource: UsageSource) {
  /**
   * @return minutes of [distractiveApps] use left at [now], counted since the later of [lastBlock]
   * and [lastUnblock] (or the last hour when there's neither). Every solved challenge, including one
   * shown on app open, starts a full interval. Zero or less means the apps should be blocked now.
   */
  fun minutesLeft(
    distractiveApps: Set<String>,
    blockIntervalMinutes: Int,
    lastBlock: Long?,
    lastUnblock: Long?,
    now: Long,
  ): Long {
    val usedMinutes = usageSource.minutesUsed(
      packages = distractiveApps,
      from = usageCountedSince(lastBlock, lastUnblock, now),
      to = now,
    )
    return blockIntervalMinutes - usedMinutes
  }
}

/**
 * When the block interval's usage starts counting: the later of the last block and the last solve,
 * or the last hour when there's neither. Shared with the dashboard so both show the same minutes.
 */
fun usageCountedSince(lastBlock: Long?, lastUnblock: Long?, now: Long): Long =
  listOfNotNull(lastBlock, lastUnblock).maxOrNull() ?: (now - ONE_HOUR)
