package app.plugbrain.android.appsusage

import app.plugbrain.android.appsusage.model.ActivitySpan
import app.plugbrain.android.appsusage.model.PhoneActivity
import app.plugbrain.android.appsusage.model.PhoneActivity.Away
import app.plugbrain.android.appsusage.model.PhoneActivity.DistractingApp
import app.plugbrain.android.appsusage.model.PhoneActivity.OtherApp
import org.junit.Assert.assertEquals
import org.junit.Test

class ActivityTimelineCalculatorTest {
    private val minute = 60_000L
    private val startTime = 0L
    private val endTime = 60 * minute
    private val distracting = "com.example.social"
    private val other = "com.example.notes"

    private fun screenOn(minutes: Long) = PhoneEvent.ScreenOn(minutes * minute)

    private fun screenOff(minutes: Long) = PhoneEvent.ScreenOff(minutes * minute)

    private fun started(
        minutes: Long,
        pkg: String,
        activity: String = "Main",
    ) = PhoneEvent.App(AppEvent(pkg, activity, EventType.APP_STARTED, minutes * minute))

    private fun stopped(
        minutes: Long,
        pkg: String,
        activity: String = "Main",
    ) = PhoneEvent.App(AppEvent(pkg, activity, EventType.APP_STOPPED, minutes * minute))

    private fun span(
        from: Long,
        to: Long,
        activity: PhoneActivity,
    ) = ActivitySpan(from * minute, to * minute, activity)

    private fun timeline(vararg events: PhoneEvent) =
        ActivityTimelineCalculator.timeline(events.toList(), setOf(distracting), startTime, endTime)

    @Test
    fun `no events at all means away`() {
        assertEquals(listOf(span(0, 60, Away)), timeline())
    }

    @Test
    fun `screen off is away, distracting app is distracting, anything else is other`() {
        assertEquals(
            listOf(span(0, 10, Away), span(10, 20, OtherApp), span(20, 40, DistractingApp), span(40, 50, OtherApp), span(50, 60, Away)),
            timeline(
                screenOn(10),
                started(10, other),
                stopped(20, other),
                started(20, distracting),
                stopped(40, distracting),
                screenOff(50),
            ),
        )
    }

    @Test
    fun `the first screen change tells the state before it`() {
        assertEquals(
            listOf(span(0, 30, DistractingApp), span(30, 60, Away)),
            timeline(stopped(30, distracting), screenOff(30)),
        )
    }

    @Test
    fun `navigating inside an app keeps it in the foreground`() {
        assertEquals(
            listOf(span(0, 10, Away), span(10, 40, DistractingApp), span(40, 60, Away)),
            timeline(
                screenOn(10),
                started(10, distracting, "Feed"),
                stopped(20, distracting, "Feed"),
                started(20, distracting, "Video"),
                stopped(21, distracting, "Feed"),
                stopped(40, distracting, "Video"),
                screenOff(40),
            ),
        )
    }

    @Test
    fun `short spans join the span before them`() {
        val glance = PhoneEvent.ScreenOff(30 * minute + 30_000)

        assertEquals(
            listOf(span(0, 60, Away)),
            timeline(screenOn(30), glance),
        )
    }
}
