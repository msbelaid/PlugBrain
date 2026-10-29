package app.plugbrain.android.service

import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.datastore.model.UserSettings
import app.plugbrain.android.util.Clock

private const val ONE_HOUR = 60 * 60_000L

/** How much of the block interval is left before distractive apps get blocked. */
class UsageLimit(
  private val appsUsageStats: AppsUsageStats,
  private val clock: Clock,
) {
  /**
   * @return minutes of distractive app use left, counted since the later of [lastBlock] and
   * [lastUnblock] (or the last hour when there's neither). Every solved challenge, including one
   * shown on app open, starts a full interval. Zero or less means the apps should be blocked now.
   */
  fun minutesLeft(settings: UserSettings, lastBlock: Long?, lastUnblock: Long?): Long {
    val now = clock.now()
    val usedMinutes = appsUsageStats.getTotalAppsUsageDuration(
      startTime = usageCountedSince(lastBlock, lastUnblock, now),
      endTime = now,
      filterPackages = settings.distractiveApps,
    )
    return settings.blockInterval - usedMinutes
  }
}

/**
 * When the block interval's usage starts counting: the later of the last block and the last solve,
 * or the last hour when there's neither. Shared with the dashboard so both show the same minutes.
 */
fun usageCountedSince(lastBlock: Long?, lastUnblock: Long?, now: Long): Long =
  listOfNotNull(lastBlock, lastUnblock).maxOrNull() ?: (now - ONE_HOUR)
