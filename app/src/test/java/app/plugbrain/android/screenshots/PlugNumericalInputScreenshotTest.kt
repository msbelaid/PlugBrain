package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.button.PlugNumericalInput
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugNumericalInputScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugNumericalInputTests() {
        paparazzi.snapshot {
            numericalInputTest()
        }
    }

    @Test
    fun plugNumericalInputDarkModeTests() {
        paparazzi.snapshot {
            numericalInputTest(darkTheme = true)
        }
    }
}

class PlugNumericalInputFontScaleScreenshotTest {
    @get:Rule
    val paparazziBigFontScale =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5.copy(fontScale = 2f),
        )

    @Test
    fun plugNumericalInputBigFontScaleTests() {
        paparazziBigFontScale.snapshot {
            numericalInputTest()
        }
    }
}

@Composable
private fun numericalInputTest(darkTheme: Boolean = false) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
        Column(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PlugNumericalInput(
                value = "",
                onValueChange = {},
                onSubmit = {},
                placeholder = "Unfocused",
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
            )
            PlugNumericalInput(
                value = "Focused",
                onValueChange = {},
                onSubmit = {},
                placeholder = "Focused",
                modifier =
                    Modifier
                        .focusRequester(focusRequester)
                        .focusable()
                        .padding(8.dp)
                        .fillMaxWidth(),
            )
            PlugNumericalInput(
                value = "Success",
                onValueChange = {},
                onSubmit = {},
                placeholder = "Success",
                isSuccess = true,
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
            )
            PlugNumericalInput(
                value = "Error",
                onValueChange = {},
                onSubmit = {},
                placeholder = "Error",
                isError = true,
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
            )
        }
    }
}
