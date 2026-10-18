package app.plugbrain.android.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.challenges.addition.AdditionChallenge
import app.plugbrain.android.ui.challenges.compose.AnswerState
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeContent
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreen
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class NumericalChallengeScreenScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    // Real challenges pick random operands, which would change the snapshot on every run.
    private val challenge =
        object : AdditionChallenge {
            override val operand1 = 25
            override val operand2 = 17
            override val difficultyLevel = 1
        }

    @Test
    fun numericalChallengeScreenTest() {
        paparazzi.snapshot {
            numericalChallengeScreenTest(darkTheme = false)
        }
    }

    @Test
    fun numericalChallengeScreenDarkTest() {
        paparazzi.snapshot {
            numericalChallengeScreenTest(darkTheme = true)
        }
    }

    @Test
    fun numericalChallengeScreenWrongAnswerTest() {
        paparazzi.snapshot {
            numericalChallengeContentTest(answer = "99", answerState = AnswerState.Wrong)
        }
    }

    @Test
    fun numericalChallengeScreenCorrectAnswerTest() {
        paparazzi.snapshot {
            numericalChallengeContentTest(answer = "42", answerState = AnswerState.Correct)
        }
    }

    @Composable
    private fun numericalChallengeScreenTest(darkTheme: Boolean = false) {
        screenshotTheme(darkTheme = darkTheme) {
            NumericalChallengeScreen(
                challenge = challenge,
                onCorrectAnswer = {},
                onExitChallenge = {},
            )
        }
    }

    @Composable
    private fun numericalChallengeContentTest(
        answer: String,
        answerState: AnswerState,
    ) {
        screenshotTheme {
            NumericalChallengeContent(
                challengeText = challenge.string(),
                answer = answer,
                answerState = answerState,
                onAnswerChange = {},
                onSubmitAnswer = {},
                onExitChallenge = {},
            )
        }
    }

    // Paparazzi doesn't enable inspection mode, which the screen relies on to skip its entrance animation.
    @Composable
    private fun screenshotTheme(
        darkTheme: Boolean = false,
        content: @Composable () -> Unit,
    ) {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme, content = content)
        }
    }
}
