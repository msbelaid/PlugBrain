package app.plugbrain.android.screenshots

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.about.AboutSheet
import app.plugbrain.android.ui.theme.PlugBrainTheme
import org.junit.Rule
import org.junit.Test

class AboutSheetScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun aboutSheetTest() {
        paparazzi.snapshotWithLargeFont {
            aboutSheet()
        }
    }

    @Test
    fun aboutSheetDarkTest() {
        paparazzi.snapshot {
            aboutSheet(darkTheme = true)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun aboutSheet(darkTheme: Boolean = false) {
        PlugBrainTheme(dynamicColor = false, darkTheme = darkTheme) {
            // The sheet animates in, so a snapshot of its first frame is blank unless it's expanded first.
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            LaunchedEffect(Unit) { sheetState.expand() }
            // A fixed version, so the snapshot doesn't change with every release.
            AboutSheet(
                versionName = "1.6.0",
                onLinkClick = {},
                onDismiss = {},
                sheetState = sheetState,
            )
        }
    }
}
