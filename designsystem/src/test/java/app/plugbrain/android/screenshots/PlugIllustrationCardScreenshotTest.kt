package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.card.PlugIllustrationCard
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugIllustrationCardScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugIllustrationCardTest() {
        paparazzi.snapshotWithLargeFont {
            illustrationCardTest()
        }
    }

    @Test
    fun plugIllustrationCardDarkModeTest() {
        paparazzi.snapshot {
            illustrationCardTest(darkTheme = true)
        }
    }

    @Composable
    private fun illustrationCardTest(darkTheme: Boolean = false) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Box(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp),
            ) {
                PlugIllustrationCard(caption = "First, find and select PlugBrain on the list of apps") {
                    repeat(3) {
                        Box(
                            modifier =
                                Modifier
                                    .width(160.dp)
                                    .height(12.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)),
                        )
                    }
                }
            }
        }
    }
}
