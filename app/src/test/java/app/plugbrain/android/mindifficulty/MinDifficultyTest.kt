package app.plugbrain.android.mindifficulty

import app.plugbrain.android.ui.mindifficulty.DifficultySnapValues
import org.junit.Assert.assertEquals
import org.junit.Test

class MinDifficultyTest {
    @Test
    fun snapValuesAreBeginnerIntermediateAndExpert() {
        assertEquals(listOf(0, 5, 10), DifficultySnapValues)
    }
}
