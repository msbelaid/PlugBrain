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
        assertEquals(2, usageLimit.minutesLeft(settings, lastBlock = null, lastUnblock = null))
    }

    @Test
    fun usingTheWholeIntervalBlocksStraightAway() {
        usage.minutes = 5
        assertEquals(0, usageLimit.minutesLeft(settings, lastBlock = null, lastUnblock = null))
    }

    @Test
    fun usageOverTheIntervalStillBlocks() {
        usage.minutes = 8
        assertEquals(-3, usageLimit.minutesLeft(settings, lastBlock = null, lastUnblock = null))
    }

    @Test
    fun usageIsCountedSinceTheLaterOfBlockAndSolve() {
        val lastBlock = NOW - 20 * 60_000L
        usageLimit.minutesLeft(settings, lastBlock, lastUnblock = lastBlock + 60_000L)
        assertEquals(Query(lastBlock + 60_000L, NOW, APPS), usage.lastQuery)
    }

    @Test
    fun aSolveWithoutABlockStartsAFullInterval() {
        // A challenge shown on app open is solved without a block before it: the old usage
        // since the last block must not end the new interval straight away.
        val lastBlock = NOW - 40 * 60_000L
        val lastUnblock = NOW - 60_000L
        usageLimit.minutesLeft(settings, lastBlock, lastUnblock)
        assertEquals(Query(lastUnblock, NOW, APPS), usage.lastQuery)
    }

    @Test
    fun aBlockAfterTheLastSolveCountsFromTheBlock() {
        val lastUnblock = NOW - 20 * 60_000L
        val lastBlock = NOW - 5 * 60_000L
        usageLimit.minutesLeft(settings, lastBlock, lastUnblock)
        assertEquals(Query(lastBlock, NOW, APPS), usage.lastQuery)
    }

    @Test
    fun aSolveBeforeAnyBlockCountsFromTheSolve() {
        val lastUnblock = NOW - 60_000L
        usageLimit.minutesLeft(settings, lastBlock = null, lastUnblock = lastUnblock)
        assertEquals(Query(lastUnblock, NOW, APPS), usage.lastQuery)
    }

    @Test
    fun withoutABlockYetTheLastHourIsCounted() {
        usageLimit.minutesLeft(settings, lastBlock = null, lastUnblock = null)
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
