package app.plugbrain.android.screenshots

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.mindifficulty.MinDifficultySheet
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class MinDifficultySheetScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun minDifficultySheetTest() {
        paparazzi.snapshotWithLargeFont {
            minDifficultySheet(minDifficulty = 5)
        }
    }

    @Test
    fun minDifficultySheetDarkTest() {
        paparazzi.snapshot {
            minDifficultySheet(minDifficulty = 7, darkTheme = true)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun minDifficultySheet(
        minDifficulty: Int,
        darkTheme: Boolean = false,
    ) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            // The sheet animates in, so a snapshot of its first frame is blank unless it's expanded first.
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            LaunchedEffect(Unit) { sheetState.expand() }
            MinDifficultySheet(
                minDifficulty = minDifficulty,
                levels = 0..15,
                // A fixed example, so the snapshot doesn't change with a random challenge.
                sampleChallenge = { "27 + 48" },
                onSave = {},
                onDismiss = {},
                sheetState = sheetState,
            )
        }
    }
}
