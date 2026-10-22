package app.plugbrain.android.ui.designsystem.components.slider

import org.junit.Assert.assertEquals
import org.junit.Test

class PlugSnapSliderPositionTest {
    private val snapValues = listOf(1, 2, 5, 10, 15, 20)

    @Test
    fun snapValuesAreEvenlySpaced() {
        snapValues.forEachIndexed { index, value -> assertEquals(index.toFloat(), snapValues.positionOf(value), 0.001f) }
    }

    @Test
    fun valuesBetweenSnapValuesShareTheirSection() {
        assertEquals(3.2f, snapValues.positionOf(11), 0.001f)
        assertEquals(1f + 2f / 3, snapValues.positionOf(4), 0.001f)
    }

    @Test
    fun everyValueCanBeReachedByDragging() {
        (1..20).forEach { assertEquals(it, snapValues.valueAt(snapValues.positionOf(it))) }
    }

    @Test
    fun theThumbIsPulledOntoNearbySnapValues() {
        assertEquals(10, snapValues.valueAt(2.9f))
        assertEquals(10, snapValues.valueAt(3.1f))
        assertEquals(11, snapValues.valueAt(3.2f))
    }
}
