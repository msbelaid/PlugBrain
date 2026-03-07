package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.card.PlugCard
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugCardScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugCardSmallNumbersLightMode() {
        paparazzi.snapshot {
            plugCardTest(challengeText = "12 + 34", darkTheme = false)
        }
    }

    @Test
    fun plugCardSmallNumbersDarkMode() {
        paparazzi.snapshot {
            plugCardTest(challengeText = "12 + 34", darkTheme = true)
        }
    }

    @Test
    fun plugCardBigNumbersLightMode() {
        paparazzi.snapshot {
            plugCardTest(challengeText = "9876 x 5432", darkTheme = false)
        }
    }

    @Test
    fun plugCardBigNumbersDarkMode() {
        paparazzi.snapshot {
            plugCardTest(challengeText = "9876 x 5432", darkTheme = true)
        }
    }

    @Test
    fun plugCardWithInputLightMode() {
        paparazzi.snapshot {
            plugCardTest(
                challengeText = "25 + 17",
                inputValue = "42",
                darkTheme = false,
            )
        }
    }

    @Test
    fun plugCardWithInputDarkMode() {
        paparazzi.snapshot {
            plugCardTest(
                challengeText = "25 + 17",
                inputValue = "42",
                darkTheme = true,
            )
        }
    }

    @Test
    fun plugCardSuccessLightMode() {
        paparazzi.snapshot {
            plugCardTest(
                challengeText = "25 + 17",
                inputValue = "42",
                isSuccess = true,
                darkTheme = false,
            )
        }
    }

    @Test
    fun plugCardSuccessDarkMode() {
        paparazzi.snapshot {
            plugCardTest(
                challengeText = "25 + 17",
                inputValue = "42",
                isSuccess = true,
                darkTheme = true,
            )
        }
    }

    @Test
    fun plugCardErrorLightMode() {
        paparazzi.snapshot {
            plugCardTest(
                challengeText = "25 + 17",
                inputValue = "99",
                isError = true,
                darkTheme = false,
            )
        }
    }

    @Test
    fun plugCardErrorDarkMode() {
        paparazzi.snapshot {
            plugCardTest(
                challengeText = "25 + 17",
                inputValue = "99",
                isError = true,
                darkTheme = true,
            )
        }
    }

    @Composable
    private fun plugCardTest(
        challengeText: String,
        darkTheme: Boolean,
        inputValue: String = "",
        isSuccess: Boolean = false,
        isError: Boolean = false,
    ) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp),
            ) {
                PlugCard(
                    challengeText = challengeText,
                    inputValue = inputValue,
                    onInputChange = {},
                    onCheckAnswer = {},
                    isSuccess = isSuccess,
                    isError = isError,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
