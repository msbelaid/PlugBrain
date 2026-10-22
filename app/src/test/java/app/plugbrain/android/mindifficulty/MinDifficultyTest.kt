package app.plugbrain.android.mindifficulty

import app.plugbrain.android.ui.mindifficulty.difficultySnapValues
import org.junit.Assert.assertEquals
import org.junit.Test

class MinDifficultyTest {
    @Test
    fun snapValuesAreTheEndsAndThePresets() {
        assertEquals(listOf(0, 1, 5, 10, 15), difficultySnapValues(0..15))
    }

    @Test
    fun presetsOutsideTheLevelsAreLeftOut() {
        assertEquals(listOf(1, 5, 8), difficultySnapValues(1..8))
    }
}
