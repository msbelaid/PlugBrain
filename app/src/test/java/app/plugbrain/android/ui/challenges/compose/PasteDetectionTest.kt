package app.plugbrain.android.ui.challenges.compose

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Text put in all at once counts as pasted (#238). */
class PasteDetectionTest {
    @Test
    fun typingOneDigitIsNotAPaste() {
        assertFalse(isPasted(old = "", new = "4"))
        assertFalse(isPasted(old = "4", new = "42"))
    }

    @Test
    fun deletingOrReplacingIsNotAPaste() {
        assertFalse(isPasted(old = "42", new = "4"))
        assertFalse(isPasted(old = "42", new = ""))
        assertFalse(isPasted(old = "42", new = "43"))
    }

    @Test
    fun severalDigitsAtOnceIsAPaste() {
        assertTrue(isPasted(old = "", new = "42"))
        assertTrue(isPasted(old = "1", new = "1234"))
    }
}
