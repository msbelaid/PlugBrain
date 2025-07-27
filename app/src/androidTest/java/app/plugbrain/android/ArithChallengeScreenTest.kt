package app.plugbrain.android

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import app.plugbrain.android.repository.model.MathChallenge
import app.plugbrain.android.repository.model.Operator
import app.plugbrain.android.ui.challenges.compose.ArithChallengeScreen
import org.junit.Rule
import org.junit.Test

class ArithChallengeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testArithChallengeScreenDisplaysOperandsAndAcceptsInput() {
        var result: Int? = null

        val challenge =
            MathChallenge(
                num1 = 7,
                num2 = 3,
                operator = Operator.ADDITION,
            )

        composeTestRule.setContent {
            ArithChallengeScreen(
                mathChallenge = challenge,
                checkAnswer = { result = it },
            )
        }

        composeTestRule.onNodeWithText("7").assertIsDisplayed()
        composeTestRule.onNodeWithText("+").assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()

        composeTestRule.onNode(hasSetTextAction()).performTextInput("10")

        composeTestRule.onNodeWithText(">").performClick()
        assert(result == 10)
    }

    @Test
    fun testArithChallengeScreenDisplaysOperandsAndAcceptsInput2() {
    }
}
