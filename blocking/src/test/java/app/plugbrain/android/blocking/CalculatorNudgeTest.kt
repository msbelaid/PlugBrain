package app.plugbrain.android.blocking

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorNudgeTest {
    @Test
    fun `the first two uses pass without a nudge`() {
        assertEquals(CalculatorNudge.None, CalculatorNudge.forUse(1))
        assertEquals(CalculatorNudge.None, CalculatorNudge.forUse(2))
    }

    @Test
    fun `the third to fifth uses show a toast`() {
        assertEquals(CalculatorNudge.Toast, CalculatorNudge.forUse(3))
        assertEquals(CalculatorNudge.Toast, CalculatorNudge.forUse(4))
        assertEquals(CalculatorNudge.Toast, CalculatorNudge.forUse(5))
    }

    @Test
    fun `every use from the sixth on shows the dialog`() {
        assertEquals(CalculatorNudge.Dialog, CalculatorNudge.forUse(6))
        assertEquals(CalculatorNudge.Dialog, CalculatorNudge.forUse(100))
    }
}
