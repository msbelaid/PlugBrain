package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.card.PlugStatusCard
import app.plugbrain.android.ui.designsystem.components.card.PlugStatusCardNotice
import app.plugbrain.android.ui.designsystem.components.card.PlugStatusCardVariant
import app.plugbrain.android.ui.theme.PlugBrainTheme
import org.junit.Rule
import org.junit.Test

class PlugStatusCardScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun protectionOnLightMode() {
        paparazzi.snapshotWithLargeFont {
            statusCardTest(darkTheme = false) { protectionOn() }
        }
    }

    @Test
    fun protectionOnDarkMode() {
        paparazzi.snapshot {
            statusCardTest(darkTheme = true) { protectionOn() }
        }
    }

    @Test
    fun appsLockedLightMode() {
        paparazzi.snapshotWithLargeFont {
            statusCardTest(darkTheme = false) { appsLocked() }
        }
    }

    @Test
    fun appsLockedDarkMode() {
        paparazzi.snapshot {
            statusCardTest(darkTheme = true) { appsLocked() }
        }
    }

    @Test
    fun protectionPausedLightMode() {
        paparazzi.snapshotWithLargeFont {
            statusCardTest(darkTheme = false) { protectionPaused() }
        }
    }

    @Test
    fun protectionPausedDarkMode() {
        paparazzi.snapshot {
            statusCardTest(darkTheme = true) { protectionPaused() }
        }
    }

    @Test
    fun nothingToBlockLightMode() {
        paparazzi.snapshotWithLargeFont {
            statusCardTest(darkTheme = false) { nothingToBlock() }
        }
    }

    @Test
    fun nothingToBlockDarkMode() {
        paparazzi.snapshot {
            statusCardTest(darkTheme = true) { nothingToBlock() }
        }
    }

    @Test
    fun withNoticeLightMode() {
        paparazzi.snapshotWithLargeFont {
            statusCardTest(darkTheme = false) { protectionOn(withNotice = true) }
        }
    }

    @Test
    fun withNoticeDarkMode() {
        paparazzi.snapshot {
            statusCardTest(darkTheme = true) { protectionOn(withNotice = true) }
        }
    }

    @Composable
    private fun protectionOn(withNotice: Boolean = false) {
        PlugStatusCard(
            label = "Protection on",
            icon = Icons.Rounded.VerifiedUser,
            headline = "11 min",
            headlineSupportingText = "left before the next challenge",
            progress = 4f / 15f,
            body = "Next challenge: level 4 of 15",
            notice =
                if (withNotice) {
                    {
                        PlugStatusCardNotice(
                            title = "Run in the background",
                            description = "Stop Android from closing PlugBrain to save battery.",
                            icon = Icons.Rounded.BatteryChargingFull,
                            onClick = {},
                        )
                    }
                } else {
                    null
                },
        )
    }

    @Composable
    private fun appsLocked() {
        PlugStatusCard(
            label = "Apps locked",
            icon = Icons.Rounded.Lock,
            headline = "Away 6 min",
            body = "Solve a level 5 challenge to unlock. The longer you stay away, the easier it gets.",
        )
    }

    @Composable
    private fun protectionPaused() {
        PlugStatusCard(
            label = "Protection paused",
            icon = Icons.Rounded.WarningAmber,
            variant = PlugStatusCardVariant.Warning,
            body = "Accessibility access was turned off, so PlugBrain can't block your apps.",
            actionText = "Turn it back on",
        )
    }

    @Composable
    private fun nothingToBlock() {
        PlugStatusCard(
            label = "Nothing to block yet",
            icon = Icons.Rounded.Apps,
            body = "Pick the apps that pull you in, and PlugBrain will ask for a challenge after 15 minutes on them.",
            actionText = "Choose apps",
        )
    }

    @Composable
    private fun statusCardTest(
        darkTheme: Boolean,
        content: @Composable () -> Unit,
    ) {
        PlugBrainTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp),
            ) {
                content()
            }
        }
    }
}
