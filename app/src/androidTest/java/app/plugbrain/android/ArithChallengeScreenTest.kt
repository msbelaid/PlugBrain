package app.plugbrain.android

import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsWithCarryChallenge
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ArithChallengeScreenTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    val screenshotOnFailure = ScreenshotOnFailureRule()

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

        typeAnswer((challenge.operand1 + challenge.operand2).toString())

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

        typeAnswer((challenge.operand1 + challenge.operand2).toString())
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
        typeAnswer("...")
        composeTestRule.onNodeWithText("Check Answer").performClick()
        composeTestRule.onNodeWithContentDescription("Input invalid").assertIsDisplayed()
        composeTestRule.waitForIdle()
        assert(!solved)
    }

    @Test
    fun pastedAnswerIsClearedWithAWarningAndANewChallenge() {
        val first = AdditionTwoDigitsWithCarryChallenge()
        var challenge by mutableStateOf(first)
        var newChallenges = 0
        var unlocked = false

        composeTestRule.setContent {
            NumericalChallengeScreen(
                challenge = challenge,
                unlockMinutes = 5,
                onUnlockApp = { unlocked = true },
                onExitChallenge = {},
                onPasteDetected = {
                    newChallenges++
                    challenge = AdditionTwoDigitsWithCarryChallenge()
                },
            )
        }

        // A paste, or the keyboard's clipboard chip, puts the whole answer in at once (#238).
        composeTestRule.onNode(hasSetTextAction()).performTextInput((first.operand1 + first.operand2).toString())

        assertPasteRejected(newChallenges)
        assert(!unlocked)
    }

    @Test
    fun answerPastedFromTheMenuIsRejected() {
        val challenge = AdditionTwoDigitsWithCarryChallenge()
        var newChallenges = 0

        composeTestRule.setContent {
            NumericalChallengeScreen(
                challenge = challenge,
                unlockMinutes = 5,
                onUnlockApp = {},
                onExitChallenge = {},
                onPasteDetected = { newChallenges++ },
            )
        }
        composeTestRule.runOnUiThread {
            val clipboard = context.getSystemService(ClipboardManager::class.java)
            clipboard.setPrimaryClip(ClipData.newPlainText("answer", (challenge.operand1 + challenge.operand2).toString()))
        }

        composeTestRule.onNode(hasSetTextAction()).performClick()
        composeTestRule.onNode(hasSetTextAction()).performSemanticsAction(SemanticsActions.PasteText)

        assertPasteRejected(newChallenges)
    }

    private fun assertPasteRejected(newChallenges: Int) {
        composeTestRule.waitForIdle()
        // The emptied field says why, in place of its usual placeholder.
        composeTestRule.onNodeWithText(context.getString(R.string.challenge_paste_warning)).assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Write your answer").assertCountEquals(0)
        assertEquals(1, newChallenges)
        // The field is empty again, so there is nothing to check.
        composeTestRule.onNodeWithText("Check Answer").assertIsNotEnabled()
        composeTestRule.onAllNodesWithText("Stay Focused").assertCountEquals(0)
    }

    // One key at a time, as a user types. Text put in all at once counts as pasted.
    private fun typeAnswer(answer: String) {
        answer.forEach { composeTestRule.onNode(hasSetTextAction()).performTextInput(it.toString()) }
    }

    // The sheet opens after a short delay that lets the success state show first.
    private fun waitForUnlockSheet() {
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithText("Stay Focused").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
