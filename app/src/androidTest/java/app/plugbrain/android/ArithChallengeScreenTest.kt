package app.plugbrain.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsWithCarryChallenge
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreen
import org.junit.Rule
import org.junit.Test

class ArithChallengeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testArithChallengeScreenDisplaysChallengeAndAcceptsCorrectAnswer() {
        var solved = false

        val challenge = AdditionTwoDigitsWithCarryChallenge()

        composeTestRule.setContent {
            NumericalChallengeScreen(
                challenge = challenge,
                onCorrectAnswer = { solved = true },
                onExitChallenge = {},
            )
        }

        composeTestRule.onNodeWithText(challenge.string()).assertIsDisplayed()

        composeTestRule.onNode(hasSetTextAction()).performTextInput((challenge.operand1 + challenge.operand2).toString())

        composeTestRule.onNodeWithText("Check Answer").performClick()
        // The screen holds the success state briefly before reporting the correct answer.
        composeTestRule.onNodeWithContentDescription("Input valid").assertIsDisplayed()
        composeTestRule.waitUntil(timeoutMillis = 5_000) { solved }
    }

    @Test
    fun testArithChallengeScreenRejectsNonNumericalInput() {
        var solved = false

        val challenge = AdditionTwoDigitsWithCarryChallenge()

        composeTestRule.setContent {
            NumericalChallengeScreen(
                challenge = challenge,
                onCorrectAnswer = { solved = true },
                onExitChallenge = {},
            )
        }
        composeTestRule.onNode(hasSetTextAction()).performTextInput("...")
        composeTestRule.onNodeWithText("Check Answer").performClick()
        composeTestRule.onNodeWithContentDescription("Input invalid").assertIsDisplayed()
        composeTestRule.waitForIdle()
        assert(!solved)
    }
}
