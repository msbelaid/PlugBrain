package app.plugbrain.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
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
    fun testCorrectAnswerShowsUnlockSheetAndUnlocks() {
        var unlocked = false
        var exited = false

        val challenge = AdditionTwoDigitsWithCarryChallenge()

        composeTestRule.setContent {
            NumericalChallengeScreen(
                challenge = challenge,
                unlockMinutes = 5,
                onUnlockApp = { unlocked = true },
                onExitChallenge = { exited = true },
            )
        }

        composeTestRule.onNodeWithText(challenge.string()).assertIsDisplayed()

        composeTestRule.onNode(hasSetTextAction()).performTextInput((challenge.operand1 + challenge.operand2).toString())

        composeTestRule.onNodeWithText("Check Answer").performClick()
        waitForUnlockSheet()
        assert(!unlocked)

        composeTestRule.onNodeWithText("Unlock App for 5 min").performClick()
        composeTestRule.waitForIdle()
        assert(unlocked)
        assert(!exited)
    }

    @Test
    fun testCorrectAnswerThenStayFocusedLeavesWithoutUnlocking() {
        var unlocked = false
        var exited = false

        val challenge = AdditionTwoDigitsWithCarryChallenge()

        composeTestRule.setContent {
            NumericalChallengeScreen(
                challenge = challenge,
                unlockMinutes = 5,
                onUnlockApp = { unlocked = true },
                onExitChallenge = { exited = true },
            )
        }

        composeTestRule.onNode(hasSetTextAction()).performTextInput((challenge.operand1 + challenge.operand2).toString())
        composeTestRule.onNodeWithText("Check Answer").performClick()
        waitForUnlockSheet()

        composeTestRule.onNodeWithText("Stay Focused").performClick()
        composeTestRule.waitForIdle()
        assert(exited)
        assert(!unlocked)
    }

    @Test
    fun testArithChallengeScreenRejectsNonNumericalInput() {
        var solved = false

        val challenge = AdditionTwoDigitsWithCarryChallenge()

        composeTestRule.setContent {
            NumericalChallengeScreen(
                challenge = challenge,
                unlockMinutes = 5,
                onUnlockApp = { solved = true },
                onExitChallenge = {},
            )
        }
        composeTestRule.onNode(hasSetTextAction()).performTextInput("...")
        composeTestRule.onNodeWithText("Check Answer").performClick()
        composeTestRule.onNodeWithContentDescription("Input invalid").assertIsDisplayed()
        composeTestRule.waitForIdle()
        assert(!solved)
    }

    // The sheet opens after a short delay that lets the success state show first.
    private fun waitForUnlockSheet() {
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithText("Stay Focused").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
