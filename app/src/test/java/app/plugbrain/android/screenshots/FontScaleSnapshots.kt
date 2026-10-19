package app.plugbrain.android.screenshots

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi

// Android's largest system font size, to catch clipped or overflowing text.
private const val LARGE_FONT_SCALE = 2f

/**
 * Takes the usual snapshot, then a second one at [LARGE_FONT_SCALE] named `<test>_fontScale2`.
 * The rule must be created with [DeviceConfig.PIXEL_5], like every screenshot test here.
 */
fun Paparazzi.snapshotWithLargeFont(composable: @Composable () -> Unit) {
    snapshot(composable = composable)
    unsafeUpdateConfig(deviceConfig = DeviceConfig.PIXEL_5.copy(fontScale = LARGE_FONT_SCALE))
    snapshot(name = "fontScale2", composable = composable)
}
