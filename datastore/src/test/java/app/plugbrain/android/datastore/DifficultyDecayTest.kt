package app.plugbrain.android.datastore

import app.plugbrain.android.datastore.model.DifficultyChange
import org.junit.Assert.assertEquals
import org.junit.Test

class DifficultyDecayTest {
    private val minute = 60_000L

    // With a 15 minute interval, the level drops by one every 30 minutes.
    private val interval = 15

    private fun project(
        vararg history: DifficultyChange,
        start: Long = 0,
        end: Long = 600 * minute,
        minDifficulty: Int = 1,
    ) = projectDifficulty(history.toList(), start, end, interval, minDifficulty)

    @Test
    fun `decay steps count whole double intervals`() {
        assertEquals(0, difficultyDecaySteps(29, interval))
        assertEquals(1, difficultyDecaySteps(30, interval))
        assertEquals(3, difficultyDecaySteps(95, interval))
        assertEquals(0, difficultyDecaySteps(95, 0))
    }

    @Test
    fun `decayed difficulty stops at the minimum`() {
        val change = DifficultyChange(0, 5)

        assertEquals(5, decayedDifficulty(change, 29 * minute, interval, minDifficulty = 1))
        assertEquals(4, decayedDifficulty(change, 30 * minute, interval, minDifficulty = 1))
        assertEquals(3, decayedDifficulty(change, 600 * minute, interval, minDifficulty = 3))
    }

    @Test
    fun `no history gives no steps`() {
        assertEquals(emptyList<DifficultyChange>(), project())
    }

    @Test
    fun `level drops every two intervals until the minimum`() {
        assertEquals(
            listOf(
                DifficultyChange(0, 3),
                DifficultyChange(30 * minute, 2),
                DifficultyChange(60 * minute, 1),
            ),
            project(DifficultyChange(0, 3)),
        )
    }

    @Test
    fun `a new change restarts the decay`() {
        assertEquals(
            listOf(
                DifficultyChange(0, 3),
                DifficultyChange(30 * minute, 2),
                DifficultyChange(40 * minute, 3),
                DifficultyChange(41 * minute, 4),
                DifficultyChange(71 * minute, 3),
                DifficultyChange(101 * minute, 2),
                DifficultyChange(131 * minute, 1),
            ),
            project(
                DifficultyChange(0, 3),
                DifficultyChange(40 * minute, 3),
                DifficultyChange(41 * minute, 4),
            ),
        )
    }

    @Test
    fun `the level at the start comes from the last change before it`() {
        assertEquals(
            listOf(
                DifficultyChange(100 * minute, 4),
                DifficultyChange(120 * minute, 3),
                DifficultyChange(150 * minute, 2),
            ),
            project(DifficultyChange(0, 4), DifficultyChange(60 * minute, 5), start = 100 * minute, end = 160 * minute),
        )
    }

    @Test
    fun `changes after the end are ignored`() {
        assertEquals(
            listOf(DifficultyChange(0, 6)),
            project(DifficultyChange(0, 6), DifficultyChange(20 * minute, 7), end = 19 * minute),
        )
    }

    @Test
    fun `a level below the minimum is raised to it once it drops`() {
        assertEquals(
            listOf(DifficultyChange(0, 2), DifficultyChange(30 * minute, 3)),
            project(DifficultyChange(0, 2), minDifficulty = 3),
        )
    }
}
