package app.plugbrain.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
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
    fun testArithChallengeScreenDisplaysOperandsAndAcceptsInput() {
        var result: Int? = null

        val challenge = AdditionTwoDigitsWithCarryChallenge()

        composeTestRule.setContent {
            NumericalChallengeScreen(
                challenge = challenge,
                checkAnswer = { result = it },
                triggerAnimation = false,
            )
        }

        composeTestRule.onNodeWithText(challenge.operand1.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText("+").assertIsDisplayed()
        composeTestRule.onNodeWithText(challenge.operand2.toString()).assertIsDisplayed()

        composeTestRule.onNode(hasSetTextAction()).performTextInput((challenge.operand1 + challenge.operand2).toString())

        composeTestRule.onNodeWithText(">").performClick()
        assert(result == challenge.operand1 + challenge.operand2)
    }
}
