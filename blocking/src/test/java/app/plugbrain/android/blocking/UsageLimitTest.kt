package app.plugbrain.android.blocking

import org.junit.Assert.assertEquals
import org.junit.Test

private const val NOW = 10 * 60 * 60_000L
private const val ONE_HOUR = 60 * 60_000L
private const val INTERVAL = 5
private val APPS = setOf("com.example.video")

class UsageLimitTest {
    private val usage = RecordingUsageSource()
    private val usageLimit = UsageLimit(usage)

    private fun minutesLeft(
        lastBlock: Long? = null,
        lastUnblock: Long? = null,
    ) = usageLimit.minutesLeft(APPS, INTERVAL, lastBlock, lastUnblock, NOW)

    @Test
    fun usageUnderTheIntervalLeavesTheRestOfIt() {
        usage.minutes = 3
        assertEquals(2, minutesLeft())
    }

    @Test
    fun usingTheWholeIntervalBlocksStraightAway() {
        usage.minutes = 5
        assertEquals(0, minutesLeft())
    }

    @Test
    fun usageOverTheIntervalStillBlocks() {
        usage.minutes = 8
        assertEquals(-3, minutesLeft())
    }

    @Test
    fun usageIsCountedSinceTheLaterOfBlockAndSolve() {
        val lastBlock = NOW - 20 * 60_000L
        minutesLeft(lastBlock, lastUnblock = lastBlock + 60_000L)
        assertEquals(Query(APPS, lastBlock + 60_000L, NOW), usage.lastQuery)
    }

    @Test
    fun aSolveWithoutABlockStartsAFullInterval() {
        // A challenge shown on app open is solved without a block before it: the old usage
        // since the last block must not end the new interval straight away.
        val lastBlock = NOW - 40 * 60_000L
        val lastUnblock = NOW - 60_000L
        minutesLeft(lastBlock, lastUnblock)
        assertEquals(Query(APPS, lastUnblock, NOW), usage.lastQuery)
    }

    @Test
    fun aBlockAfterTheLastSolveCountsFromTheBlock() {
        val lastUnblock = NOW - 20 * 60_000L
        val lastBlock = NOW - 5 * 60_000L
        minutesLeft(lastBlock, lastUnblock)
        assertEquals(Query(APPS, lastBlock, NOW), usage.lastQuery)
    }

    @Test
    fun aSolveBeforeAnyBlockCountsFromTheSolve() {
        val lastUnblock = NOW - 60_000L
        minutesLeft(lastUnblock = lastUnblock)
        assertEquals(Query(APPS, lastUnblock, NOW), usage.lastQuery)
    }

    @Test
    fun withoutABlockYetTheLastHourIsCounted() {
        minutesLeft()
        assertEquals(Query(APPS, NOW - ONE_HOUR, NOW), usage.lastQuery)
    }

    private data class Query(val packages: Set<String>, val from: Long, val to: Long)

    private class RecordingUsageSource : UsageSource {
        var minutes = 0L
        var lastQuery: Query? = null

        override fun minutesUsed(
            packages: Set<String>,
            from: Long,
            to: Long,
        ): Long {
            lastQuery = Query(packages, from, to)
            return minutes
        }
    }
}
