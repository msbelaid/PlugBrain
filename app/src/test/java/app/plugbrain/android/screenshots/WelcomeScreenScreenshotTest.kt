package app.plugbrain.android.screenshots

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.onboarding.WelcomeScreen
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class WelcomeScreenScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun welcomeScreenTest() {
        paparazzi.snapshot {
            welcomeScreenTest(darkTheme = false)
        }
    }

    @Test
    fun welcomeScreenDarkTest() {
        paparazzi.snapshot {
            welcomeScreenTest(darkTheme = true)
        }
    }

    @Composable
    private fun welcomeScreenTest(darkTheme: Boolean = false) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            WelcomeScreen()
        }
    }
}
