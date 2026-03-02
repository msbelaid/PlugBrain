package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonPrimary
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonSecondary
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonTertiary
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugButtonScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugButtonTests() {
        paparazzi.snapshot {
            plugButtonsTest(darkTheme = false)
        }
    }

    @Test
    fun plugButtonDarkModeTests() {
        paparazzi.snapshot {
            plugButtonsTest(darkTheme = true)
        }
    }

    @Composable
    private fun plugButtonsTest(darkTheme: Boolean) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                PlugButtonPrimary(
                    text = "Primary Button with icon",
                    onClick = {},
                    icon = Icons.Default.Settings,
                    modifier = Modifier.fillMaxWidth(),
                )
                PlugButtonPrimary(
                    text = "Primary Button disabled",
                    onClick = {},
                    enabled = false,
                    icon = Icons.Default.Settings,
                    modifier = Modifier.fillMaxWidth(),
                )
                PlugButtonSecondary(
                    text = "Secondary Button",
                    onClick = {},
                    icon = Icons.Default.Settings,
                    modifier = Modifier.fillMaxWidth(),
                )
                PlugButtonSecondary(
                    text = "Secondary Button",
                    onClick = {},
                    enabled = false,
                    icon = Icons.Default.Settings,
                    modifier = Modifier.fillMaxWidth(),
                )
                PlugButtonTertiary(
                    text = "Tertiary Button",
                    onClick = {},
                    icon = Icons.Default.Settings,
                    modifier = Modifier.fillMaxWidth(),
                )
                PlugButtonTertiary(
                    text = "Tertiary Button",
                    onClick = {},
                    enabled = false,
                    icon = Icons.Default.Settings,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
