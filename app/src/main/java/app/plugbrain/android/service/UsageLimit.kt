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
   * @return minutes of distractive app use left, counted since [lastBlock] (or the last hour when
   * apps were never blocked). Zero or less means the apps should be blocked now.
   */
  fun minutesLeft(settings: UserSettings, lastBlock: Long?): Long {
    val now = clock.now()
    val usedMinutes = appsUsageStats.getTotalAppsUsageDuration(
      startTime = lastBlock ?: (now - ONE_HOUR),
      endTime = now,
      filterPackages = settings.distractiveApps,
    )
    return settings.blockInterval - usedMinutes
  }
}
