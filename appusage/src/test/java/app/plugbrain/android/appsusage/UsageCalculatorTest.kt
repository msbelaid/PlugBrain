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

    private fun paused(
        timestamp: Long,
        activity: String = "Main",
        pkg: String = app,
    ) = AppEvent(pkg, activity, EventType.APP_PAUSED, timestamp)

    private fun stopped(
        timestamp: Long,
        activity: String = "Main",
        pkg: String = app,
    ) = AppEvent(pkg, activity, EventType.APP_STOPPED, timestamp)

    private fun screenOff(timestamp: Long) = AppEvent("android", "", EventType.SCREEN_OFF, timestamp)

    private fun total(vararg events: AppEvent) = UsageCalculator.totalTimePerPackage(events.toList(), startTime, endTime)

    @Test
    fun `no events returns empty map`() {
        assertEquals(emptyMap<String, Long>(), total())
    }

    @Test
    fun `single session is counted`() {
        assertEquals(mapOf(app to 1_000L), total(started(2_000), paused(3_000)))
    }

    @Test
    fun `pause then stop of the same activity is counted once`() {
        assertEquals(mapOf(app to 1_000L), total(started(2_000), paused(3_000), stopped(3_500)))
    }

    @Test
    fun `multiple sessions are summed`() {
        assertEquals(
            mapOf(app to 1_500L),
            total(started(2_000), paused(3_000), started(5_000), paused(5_500)),
        )
    }

    @Test
    fun `app in foreground at window start is counted from start time`() {
        assertEquals(mapOf(app to 1_000L), total(paused(2_000)))
    }

    @Test
    fun `app in foreground at window start with pause then stop is counted once`() {
        assertEquals(mapOf(app to 1_000L), total(paused(2_000), stopped(2_500)))
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
                paused(3_000, "A"),
                started(3_010, "B"),
                stopped(3_500, "A"),
                paused(6_000, "B"),
            ),
        )
    }

    @Test
    fun `navigating between activities with B still open at end counts until end time`() {
        assertEquals(
            mapOf(app to 7_990L),
            total(started(2_000, "A"), paused(3_000, "A"), started(3_010, "B"), stopped(3_500, "A")),
        )
    }

    @Test
    fun `stop without a pause ends the session`() {
        assertEquals(mapOf(app to 1_000L), total(started(2_000), stopped(3_000)))
    }

    @Test
    fun `reopening the same activity keeps the session open`() {
        // A new instance of Main replaces the old one: Main paused -> Main resumed -> old Main stopped.
        assertEquals(
            mapOf(app to 7_990L),
            total(started(2_000), paused(3_000), started(3_010), stopped(3_500)),
        )
    }

    @Test
    fun `reopening the same activity again and again counts all the time`() {
        // The blocking smoke test reopens Settings every 20 s.
        assertEquals(
            mapOf(app to 60 * MINUTE - 20L),
            UsageCalculator.totalTimePerPackage(
                listOf(
                    started(0),
                    paused(20 * MINUTE),
                    started(20 * MINUTE + 10),
                    stopped(20 * MINUTE + 500),
                    paused(40 * MINUTE),
                    started(40 * MINUTE + 10),
                    stopped(40 * MINUTE + 500),
                ),
                0,
                60 * MINUTE,
            ),
        )
    }

    @Test
    fun `packages are counted separately`() {
        assertEquals(
            mapOf(app to 1_000L, otherApp to 500L),
            total(
                started(2_000),
                paused(3_000),
                started(3_000, pkg = otherApp),
                paused(3_500, pkg = otherApp),
            ),
        )
    }

    @Test
    fun `resume without a pause ends when another app is resumed`() {
        assertEquals(
            mapOf(app to 1_000L, otherApp to 1_000L),
            total(started(2_000), started(3_000, pkg = otherApp), paused(4_000, pkg = otherApp)),
        )
    }

    @Test
    fun `pause with another class name ends when another app is resumed`() {
        assertEquals(
            mapOf(app to 2_000L, otherApp to 500L),
            total(
                started(2_000, "Gallery"),
                paused(3_000, "GalleryAlias"),
                started(4_000, pkg = otherApp),
                paused(4_500, pkg = otherApp),
            ),
        )
    }

    @Test
    fun `resume without a pause ends when the screen turns off`() {
        assertEquals(mapOf(app to 1_000L), total(started(2_000), screenOff(3_000)))
    }

    @Test
    fun `late pause after another app was resumed is ignored`() {
        assertEquals(
            mapOf(app to 1_000L, otherApp to 7_000L),
            total(started(2_000), started(3_000, pkg = otherApp), paused(3_100)),
        )
    }

    @Test
    fun `app in foreground at window start ends when another app is resumed first`() {
        assertEquals(
            mapOf(app to 1_000L, otherApp to 500L),
            total(started(2_000, pkg = otherApp), paused(2_500, pkg = otherApp), paused(5_000)),
        )
    }

    @Test
    fun `app in foreground at window start ends when the screen turns off`() {
        assertEquals(mapOf(app to 500L), total(screenOff(1_500), paused(5_000)))
    }

    @Test
    fun `screen off alone counts nothing`() {
        assertEquals(emptyMap<String, Long>(), total(screenOff(2_000)))
    }

    @Test
    fun `late pause after the screen turned off is not counted again`() {
        assertEquals(mapOf(app to 1_000L), total(started(2_000), screenOff(3_000), paused(3_200)))
    }

    @Test
    fun `screen off time between two sessions of the same app is not counted`() {
        // The screen turns off with the app open, then the user unlocks back into it.
        assertEquals(
            mapOf(app to 2_000L),
            total(started(2_000), screenOff(3_000), started(8_000), paused(9_000)),
        )
    }

    @Test
    fun `resume after a shutdown starts a new session`() {
        // Shutdown arrives as SCREEN_OFF, and the app was never paused before it.
        assertEquals(
            mapOf(app to 1_500L),
            total(started(2_000), screenOff(2_500), started(6_000), paused(7_000)),
        )
    }

    @Test
    fun `resuming the same activity twice keeps the first start`() {
        assertEquals(mapOf(app to 2_000L), total(started(2_000), started(3_000), paused(4_000)))
    }

    @Test
    fun `switching back and forth between two apps counts each visit once`() {
        assertEquals(
            mapOf(app to 3_000L, otherApp to 2_000L),
            total(
                started(2_000),
                started(3_000, pkg = otherApp),
                paused(3_050),
                started(4_000),
                paused(4_050, pkg = otherApp),
                started(5_000, pkg = otherApp),
                paused(5_050),
                started(6_000),
                paused(7_000),
            ),
        )
    }

    @Test
    fun `session across midnight is counted whole`() {
        val midnight = 1_700_006_400_000L
        val events =
            listOf(
                started(midnight - 10 * MINUTE),
                paused(midnight + 5 * MINUTE),
            )

        val result = UsageCalculator.totalTimePerPackage(events, midnight - HOUR, midnight + HOUR)

        assertEquals(mapOf(app to 15 * MINUTE), result)
    }

    @Test
    fun `window starting at midnight counts only the time after midnight`() {
        val midnight = 1_700_006_400_000L
        // The app was opened before midnight, so only its pause falls in the window.
        val events = listOf(paused(midnight + 5 * MINUTE))

        val result = UsageCalculator.totalTimePerPackage(events, midnight, midnight + HOUR)

        assertEquals(mapOf(app to 5 * MINUTE), result)
    }

    @Test
    fun `app left open overnight with the screen off counts only until the screen turned off`() {
        // The 16h bug: a resume without a pause used to run until the end of the window.
        val evening = 1_700_000_000_000L
        val events = listOf(started(evening), screenOff(evening + 20 * MINUTE))

        val result = UsageCalculator.totalTimePerPackage(events, evening - HOUR, evening + 16 * HOUR)

        assertEquals(mapOf(app to 20 * MINUTE), result)
    }

    private companion object {
        const val MINUTE = 60_000L
        const val HOUR = 60 * MINUTE
    }
}
