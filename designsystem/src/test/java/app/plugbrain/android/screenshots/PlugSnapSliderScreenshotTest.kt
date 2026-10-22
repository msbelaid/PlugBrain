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
import app.plugbrain.android.ui.designsystem.components.slider.PlugSnapSlider
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugSnapSliderScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugSnapSliderTest() {
        paparazzi.snapshotWithLargeFont {
            snapSliderTest()
        }
    }

    @Test
    fun plugSnapSliderDarkModeTest() {
        paparazzi.snapshot {
            snapSliderTest(darkTheme = true)
        }
    }

    @Composable
    private fun snapSliderTest(darkTheme: Boolean = false) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp),
            ) {
                listOf(1, 5, 12, 20).forEach { value ->
                    PlugSnapSlider(
                        value = value,
                        snapValues = listOf(1, 2, 5, 10, 15, 20),
                        onValueChange = {},
                    )
                }
            }
        }
    }
}
