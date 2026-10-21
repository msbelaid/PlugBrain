package app.plugbrain.android.main

import app.plugbrain.android.appsusage.model.ActivitySpan
import app.plugbrain.android.appsusage.model.PhoneActivity
import app.plugbrain.android.datastore.model.DifficultyChange
import app.plugbrain.android.ui.main.presentation.TIMELINE_DURATION
import app.plugbrain.android.ui.main.presentation.difficultyTimeline
import app.plugbrain.android.ui.main.presentation.timelineHourTicks
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.TimeZone

class DifficultyTimelineTest {
    private val minute = 60_000L
    private val hour = 60 * minute

    // 2026-10-21 08:45 UTC
    private val now = 1_792_572_300_000L
    private val utc = TimeZone.getTimeZone("UTC")

    private fun timeline(
        history: List<DifficultyChange>,
        storedLevel: Int = 4,
        lastChallenge: Long? = null,
        activity: List<ActivitySpan> = emptyList(),
    ) = difficultyTimeline(
        history = history,
        storedLevel = storedLevel,
        lastChallenge = lastChallenge,
        activity = activity,
        now = now,
        blockInterval = 15,
        minDifficulty = 1,
    )

    @Test
    fun `covers the last 24 hours`() {
        val result = timeline(listOf(DifficultyChange(now - hour, 2)))

        assertEquals(now - TIMELINE_DURATION, result.start)
        assertEquals(now, result.end)
    }

    @Test
    fun `without a logged change the stored level starts at the last challenge`() {
        val result = timeline(history = emptyList(), storedLevel = 4, lastChallenge = now - 40 * minute)

        assertEquals(
            listOf(DifficultyChange(now - 40 * minute, 4), DifficultyChange(now - 10 * minute, 3)),
            result.levels,
        )
        assertEquals(3, result.currentLevel)
    }

    @Test
    fun `lowest, highest and time spent come from the steps and spans`() {
        val result =
            timeline(
                history = listOf(DifficultyChange(now - 3 * hour, 6), DifficultyChange(now - 2 * hour, 2)),
                activity =
                    listOf(
                        ActivitySpan(now - 3 * hour, now - hour, PhoneActivity.Away),
                        ActivitySpan(now - hour, now - 30 * minute, PhoneActivity.DistractingApp),
                        ActivitySpan(now - 30 * minute, now, PhoneActivity.Away),
                    ),
            )

        assertEquals(1, result.lowestLevel)
        assertEquals(6, result.highestLevel)
        assertEquals(150 * minute, result.millisSpent(PhoneActivity.Away))
        assertEquals(30 * minute, result.millisSpent(PhoneActivity.DistractingApp))
    }

    @Test
    fun `hour ticks fall every six hours away from both ends`() {
        val ticks = timelineHourTicks(now - TIMELINE_DURATION, now, utc)

        // The window runs from 08:45 yesterday: 10:00 and 08:00 today are closer than two hours to an end.
        val midnight = now - 8 * hour - 45 * minute
        assertEquals(listOf(midnight - 12 * hour, midnight - 6 * hour, midnight, midnight + 6 * hour), ticks)
    }
}
