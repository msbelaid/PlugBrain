package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.listitem.PlugAppCheckboxRow
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugAppCheckboxRowScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugAppCheckboxRowTest() {
        paparazzi.snapshotWithLargeFont {
            appCheckboxRowTest()
        }
    }

    @Test
    fun plugAppCheckboxRowDarkModeTest() {
        paparazzi.snapshot {
            appCheckboxRowTest(darkTheme = true)
        }
    }

    @Composable
    private fun appCheckboxRowTest(darkTheme: Boolean = false) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
                PlugAppCheckboxRow(
                    name = "Subway Surfer",
                    icon = rememberVectorPainter(Icons.Rounded.Android),
                    isChecked = true,
                    onCheckedChange = {},
                )
                PlugAppCheckboxRow(
                    name = "Instagram",
                    icon = rememberVectorPainter(Icons.Rounded.Android),
                    isChecked = false,
                    onCheckedChange = {},
                )
                PlugAppCheckboxRow(
                    name = "Youtube",
                    icon = rememberVectorPainter(Icons.Rounded.Android),
                    isChecked = false,
                    onCheckedChange = {},
                    supportingContent = {
                        Text(
                            text = "2h 15m",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                )
            }
        }
    }
}
