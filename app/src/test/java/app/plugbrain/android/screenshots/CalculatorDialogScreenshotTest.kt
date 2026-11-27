package app.plugbrain.android.screenshots

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.calculator.CalculatorDialog
import app.plugbrain.android.ui.theme.PlugBrainTheme
import org.junit.Rule
import org.junit.Test

class CalculatorDialogScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            environment = paparazziEnvironment(),
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun calculatorDialogTest() {
        paparazzi.snapshotWithLargeFont {
            calculatorDialog()
        }
    }

    @Test
    fun calculatorDialogDarkTest() {
        paparazzi.snapshot {
            calculatorDialog(darkTheme = true)
        }
    }

    @Composable
    private fun calculatorDialog(darkTheme: Boolean = false) {
        PlugBrainTheme(dynamicColor = false, darkTheme = darkTheme) {
            CalculatorDialog(onBackToChallenge = {}, onUseCalculator = {})
        }
    }
}
