package app.plugbrain.android.service

import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.datastore.model.UserSettings
import org.junit.Assert.assertEquals
import org.junit.Test

private const val NOW = 10 * 60 * 60_000L
private const val ONE_HOUR = 60 * 60_000L
private val APPS = setOf("com.example.video")

class UsageLimitTest {
    private val usage = RecordingUsageStats()
    private val usageLimit = UsageLimit(usage) { NOW }
    private val settings = UserSettings(blockInterval = 5, distractiveApps = APPS)

    @Test
    fun usageUnderTheIntervalLeavesTheRestOfIt() {
        usage.minutes = 3
        assertEquals(2, usageLimit.minutesLeft(settings, lastBlock = null))
    }

    @Test
    fun usingTheWholeIntervalBlocksStraightAway() {
        usage.minutes = 5
        assertEquals(0, usageLimit.minutesLeft(settings, lastBlock = null))
    }

    @Test
    fun usageOverTheIntervalStillBlocks() {
        usage.minutes = 8
        assertEquals(-3, usageLimit.minutesLeft(settings, lastBlock = null))
    }

    @Test
    fun usageIsCountedSinceTheLastBlock() {
        val lastBlock = NOW - 20 * 60_000L
        usageLimit.minutesLeft(settings, lastBlock)
        assertEquals(Query(lastBlock, NOW, APPS), usage.lastQuery)
    }

    @Test
    fun withoutABlockYetTheLastHourIsCounted() {
        usageLimit.minutesLeft(settings, lastBlock = null)
        assertEquals(Query(NOW - ONE_HOUR, NOW, APPS), usage.lastQuery)
    }

    private data class Query(val startTime: Long, val endTime: Long, val packages: Set<String>)

    private class RecordingUsageStats : AppsUsageStats {
        var minutes = 0L
        var lastQuery: Query? = null

        override fun getTotalAppsUsageDuration(
            startTime: Long,
            endTime: Long,
            filterPackages: Set<String>,
        ): Long {
            lastQuery = Query(startTime, endTime, filterPackages)
            return minutes
        }

        override fun getUsagePerPackage(
            startTime: Long,
            endTime: Long,
            filterPackages: Set<String>,
        ): Map<String, Long> = emptyMap()
    }
}
