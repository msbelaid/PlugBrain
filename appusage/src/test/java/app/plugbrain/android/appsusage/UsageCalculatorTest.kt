package app.plugbrain.android.appsusage

import org.junit.Assert.assertEquals
import org.junit.Test

class UsageCalculatorTest {
    private val startTime = 1_000L
    private val endTime = 10_000L
    private val app = "com.example.app"
    private val otherApp = "com.example.other"

    private fun started(
        timestamp: Long,
        activity: String = "Main",
        pkg: String = app,
    ) = AppEvent(pkg, activity, EventType.APP_STARTED, timestamp)

    private fun stopped(
        timestamp: Long,
        activity: String = "Main",
        pkg: String = app,
    ) = AppEvent(pkg, activity, EventType.APP_STOPPED, timestamp)

    private fun total(vararg events: AppEvent) = UsageCalculator.totalTimePerPackage(events.toList(), startTime, endTime)

    @Test
    fun `no events returns empty map`() {
        assertEquals(emptyMap<String, Long>(), total())
    }

    @Test
    fun `single session is counted`() {
        assertEquals(mapOf(app to 1_000L), total(started(2_000), stopped(3_000)))
    }

    @Test
    fun `pause then stop of the same activity is counted once`() {
        assertEquals(mapOf(app to 1_000L), total(started(2_000), stopped(3_000), stopped(3_500)))
    }

    @Test
    fun `multiple sessions are summed`() {
        assertEquals(
            mapOf(app to 1_500L),
            total(started(2_000), stopped(3_000), started(5_000), stopped(5_500)),
        )
    }

    @Test
    fun `app in foreground at window start is counted from start time`() {
        assertEquals(mapOf(app to 1_000L), total(stopped(2_000)))
    }

    @Test
    fun `app in foreground at window start with pause then stop is counted once`() {
        assertEquals(mapOf(app to 1_000L), total(stopped(2_000), stopped(2_500)))
    }

    @Test
    fun `app still in foreground at window end is counted until end time`() {
        assertEquals(mapOf(app to 2_000L), total(started(8_000)))
    }

    @Test
    fun `navigating between activities keeps the session open`() {
        // A paused -> B resumed -> A stopped -> B paused
        assertEquals(
            mapOf(app to 3_990L),
            total(
                started(2_000, "A"),
                stopped(3_000, "A"),
                started(3_010, "B"),
                stopped(3_500, "A"),
                stopped(6_000, "B"),
            ),
        )
    }

    @Test
    fun `navigating between activities with B still open at end counts until end time`() {
        assertEquals(
            mapOf(app to 7_990L),
            total(started(2_000, "A"), stopped(3_000, "A"), started(3_010, "B"), stopped(3_500, "A")),
        )
    }

    @Test
    fun `packages are counted separately`() {
        assertEquals(
            mapOf(app to 1_000L, otherApp to 500L),
            total(
                started(2_000),
                stopped(3_000),
                started(3_000, pkg = otherApp),
                stopped(3_500, pkg = otherApp),
            ),
        )
    }
}
