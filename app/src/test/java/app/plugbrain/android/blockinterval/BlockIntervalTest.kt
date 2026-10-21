package app.plugbrain.android.blockinterval

import app.plugbrain.android.ui.blockinterval.BlockIntervalSnapValues
import app.plugbrain.android.ui.blockinterval.nearestBlockInterval
import org.junit.Assert.assertEquals
import org.junit.Test

class BlockIntervalTest {
    @Test
    fun snapValuesAreKeptAsIs() {
        BlockIntervalSnapValues.forEach { assertEquals(it, nearestBlockInterval(it)) }
    }

    @Test
    fun oldIntervalsOpenOnTheClosestSnapValue() {
        assertEquals(8, nearestBlockInterval(10))
        assertEquals(13, nearestBlockInterval(15))
        assertEquals(21, nearestBlockInterval(20))
    }

    @Test
    fun outOfRangeIntervalsOpenOnTheEnds() {
        assertEquals(1, nearestBlockInterval(0))
        assertEquals(21, nearestBlockInterval(60))
    }
}
