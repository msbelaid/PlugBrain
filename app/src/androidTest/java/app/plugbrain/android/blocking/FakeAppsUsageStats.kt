package app.plugbrain.android.blocking

import app.plugbrain.android.appsusage.AppsUsageStats

/** Reports [minutes] of use for any window, so a test reaches the block interval without waiting. */
object FakeAppsUsageStats : AppsUsageStats {
    @Volatile
    var minutes = 0L

    /** How many times MathLockService asked for usage, which shows it saw the app open. */
    @Volatile
    var queries = 0
        private set

    override fun getTotalAppsUsageDuration(
        startTime: Long,
        endTime: Long,
        filterPackages: Set<String>,
    ): Long {
        queries++
        return minutes
    }

    override fun getUsagePerPackage(
        startTime: Long,
        endTime: Long,
        filterPackages: Set<String>,
    ): Map<String, Long> = filterPackages.associateWith { minutes * 60_000L }

    fun reset() {
        minutes = 0
        queries = 0
    }
}
