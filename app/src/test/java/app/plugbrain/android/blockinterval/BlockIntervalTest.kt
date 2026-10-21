package app.plugbrain.android.blockinterval

import app.plugbrain.android.ui.blockinterval.sliderBlockInterval
import org.junit.Assert.assertEquals
import org.junit.Test

class BlockIntervalTest {
    @Test
    fun intervalsInRangeAreKeptAsIs() {
        (1..20).forEach { assertEquals(it, sliderBlockInterval(it)) }
    }

    @Test
    fun outOfRangeIntervalsOpenOnTheEnds() {
        assertEquals(1, sliderBlockInterval(0))
        assertEquals(20, sliderBlockInterval(21))
    }
}
