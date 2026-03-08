package app.plugbrain.android.screenshots

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.sheetdrawer.PlugBottomSheetDrawer
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugBottomSheetScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugBottomSheetInputTests() {
        paparazzi.snapshot {
            bottomSheetTest()
        }
    }

    @Test
    fun plugBottomSheetDarkModeTests() {
        paparazzi.snapshot {
            bottomSheetTest(darkTheme = true)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun bottomSheetTest(darkTheme: Boolean = false) {
        val sheetState = rememberModalBottomSheetState()

        LaunchedEffect(Unit) { sheetState.expand() }

        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            PlugBottomSheetDrawer(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = stringResource(R.string.settings_title),
                            modifier =
                                Modifier
                                    .size(40.dp)
                                    .padding(end = 8.dp),
                        )
                        Text(
                            text = "Correct Answer",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                },
                primaryCtaText = "Unlock app for 5 mins",
                primaryCtaAction = { },
                primaryCtaIcon = Icons.Rounded.Clear,
                secondaryCtaText = "Stay Focused!",
                secondaryCtaAction = { },
                secondaryCtaIcon = Icons.Rounded.Check,
                onDismiss = { },
                sheetState = sheetState,
            ) {
                Text(
                    text = "Click to unlock the app, or better : leave the app",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}
