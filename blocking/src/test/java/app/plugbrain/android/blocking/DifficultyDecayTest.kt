package app.plugbrain.android.blocking

import org.junit.Assert.assertEquals
import org.junit.Test

private const val MINUTE = 60_000L
private const val NOW = 1_000 * MINUTE
private const val INTERVAL = 5

class DifficultyDecayTest {
    private fun levelsToDropAfter(minutes: Long) = DifficultyDecay.levelsToDrop(NOW - minutes * MINUTE, INTERVAL, NOW)

    @Test
    fun `a challenge within two intervals keeps the level`() {
        assertEquals(0, levelsToDropAfter(0))
        assertEquals(0, levelsToDropAfter(9))
    }

    @Test
    fun `exactly two intervals drop one level`() {
        assertEquals(1, levelsToDropAfter(10))
    }

    @Test
    fun `every further two intervals drop one more level`() {
        assertEquals(2, levelsToDropAfter(20))
        assertEquals(2, levelsToDropAfter(29))
        assertEquals(5, levelsToDropAfter(50))
    }

    @Test
    fun `parts of a minute do not count`() {
        assertEquals(0, DifficultyDecay.levelsToDrop(NOW - 10 * MINUTE + 1, INTERVAL, NOW))
    }

    @Test
    fun `without any challenge yet the level drops as far as it can`() {
        assertEquals(100, DifficultyDecay.levelsToDrop(lastChallenge = null, blockIntervalMinutes = INTERVAL, now = NOW))
    }
}
