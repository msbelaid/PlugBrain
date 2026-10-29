package app.plugbrain.android.screenshots

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AccessibilityNew
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonTertiary
import app.plugbrain.android.ui.designsystem.components.layout.PlugStepScaffold
import app.plugbrain.android.ui.designsystem.components.listitem.PlugPermissionListItem
import app.plugbrain.android.ui.theme.PlugBrainTheme
import org.junit.Rule
import org.junit.Test

class PlugStepScaffoldScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugStepScaffoldTest() {
        paparazzi.snapshotWithLargeFont {
            stepScaffoldTest()
        }
    }

    @Test
    fun plugStepScaffoldDarkModeTest() {
        paparazzi.snapshot {
            stepScaffoldTest(darkTheme = true)
        }
    }

    @Composable
    private fun stepScaffoldTest(darkTheme: Boolean = false) {
        PlugBrainTheme(dynamicColor = false, darkTheme = darkTheme) {
            PlugStepScaffold(
                title = "Let's get set up",
                subtitle = "PlugBrain needs the following permissions to work correctly.",
                currentStep = 1,
                totalSteps = 3,
                primaryCtaText = "Proceed",
                onPrimaryCtaClick = {},
                isPrimaryCtaEnabled = false,
                helperText = "Allow the required permissions to proceed.",
                topAction = { PlugButtonTertiary(text = "Skip", onClick = {}) },
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PlugPermissionListItem(
                        title = "App Usage access",
                        description = "Lets PlugBrain track app usage so it can block distracting apps at the right time.",
                        icon = Icons.Filled.Search,
                        isGranted = true,
                        onClick = {},
                    )
                    PlugPermissionListItem(
                        title = "Display over other apps",
                        description = "Allows PlugBrain to display a challenge while you use a distracting app.",
                        icon = Icons.Rounded.Layers,
                        isGranted = false,
                        onClick = {},
                    )
                    PlugPermissionListItem(
                        title = "Accessibility permission",
                        description = "Allows PlugBrain to detect when apps are open.",
                        icon = Icons.Rounded.AccessibilityNew,
                        isGranted = false,
                        onClick = {},
                    )
                    PlugPermissionListItem(
                        title = "Battery optimization",
                        description = "Ensures that PlugBrain keeps running reliably even when your device tries to save battery.",
                        icon = Icons.Rounded.BatteryChargingFull,
                        isGranted = false,
                        onClick = {},
                    )
                }
            }
        }
    }
}
