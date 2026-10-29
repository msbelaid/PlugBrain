package app.plugbrain.android.blocking

import app.plugbrain.android.appsusage.AppsUsageStats

/** Reports [minutes] of use for any window, so a test reaches the block interval without waiting. */
object FakeAppsUsageStats : AppsUsageStats {
    @Volatile
    var minutes = 0L

    /** When the [minutes] of use ended: a window that starts at or after it saw no use. */
    @Volatile
    var usedBefore = Long.MAX_VALUE

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
        return if (startTime < usedBefore) minutes else 0
    }

    override fun getUsagePerPackage(
        startTime: Long,
        endTime: Long,
        filterPackages: Set<String>,
    ): Map<String, Long> = filterPackages.associateWith { if (startTime < usedBefore) minutes * 60_000L else 0 }

    fun reset() {
        minutes = 0
        usedBefore = Long.MAX_VALUE
        queries = 0
    }
}
