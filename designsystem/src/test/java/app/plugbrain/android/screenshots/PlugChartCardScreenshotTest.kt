package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.chart.SampleTimelineCard
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugChartCardScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun timelineCardLightMode() {
        paparazzi.snapshotWithLargeFont {
            chartCardTest(darkTheme = false)
        }
    }

    @Test
    fun timelineCardDarkMode() {
        paparazzi.snapshot {
            chartCardTest(darkTheme = true)
        }
    }

    @Composable
    private fun chartCardTest(darkTheme: Boolean) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp),
            ) {
                SampleTimelineCard()
            }
        }
    }
}
