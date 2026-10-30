package app.plugbrain.android.screenshots

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.blockinterval.BlockIntervalSheet
import app.plugbrain.android.ui.theme.PlugBrainTheme
import org.junit.Rule
import org.junit.Test

class BlockIntervalSheetScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            environment = paparazziEnvironment(),
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun blockIntervalSheetTest() {
        paparazzi.snapshotWithLargeFont {
            blockIntervalSheet(blockInterval = 12, challengeOnAppOpen = true)
        }
    }

    @Test
    fun blockIntervalSheetDarkTest() {
        paparazzi.snapshot {
            blockIntervalSheet(blockInterval = 1, darkTheme = true)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun blockIntervalSheet(
        blockInterval: Int,
        challengeOnAppOpen: Boolean = false,
        darkTheme: Boolean = false,
    ) {
        PlugBrainTheme(dynamicColor = false, darkTheme = darkTheme) {
            // The sheet animates in, so a snapshot of its first frame is blank unless it's expanded first.
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            LaunchedEffect(Unit) { sheetState.expand() }
            BlockIntervalSheet(
                blockInterval = blockInterval,
                challengeOnAppOpen = challengeOnAppOpen,
                onSave = { _, _ -> },
                onDismiss = {},
                sheetState = sheetState,
            )
        }
    }
}
