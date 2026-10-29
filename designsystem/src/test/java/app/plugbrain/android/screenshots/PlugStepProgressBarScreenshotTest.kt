package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.progress.PlugStepProgressBar
import app.plugbrain.android.ui.theme.PlugBrainTheme
import org.junit.Rule
import org.junit.Test

class PlugStepProgressBarScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugStepProgressBarTest() {
        paparazzi.snapshot {
            stepProgressBarTest()
        }
    }

    @Test
    fun plugStepProgressBarDarkModeTest() {
        paparazzi.snapshot {
            stepProgressBarTest(darkTheme = true)
        }
    }

    @Composable
    private fun stepProgressBarTest(darkTheme: Boolean = false) {
        PlugBrainTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp),
            ) {
                PlugStepProgressBar(currentStep = 0, totalSteps = 3)
                PlugStepProgressBar(currentStep = 1, totalSteps = 3)
                PlugStepProgressBar(currentStep = 2, totalSteps = 3)
                PlugStepProgressBar(currentStep = 3, totalSteps = 3)
            }
        }
    }
}
