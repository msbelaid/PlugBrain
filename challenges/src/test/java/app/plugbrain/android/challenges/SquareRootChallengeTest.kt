package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.square.SquareRootChallengeElevenThroughTwenty
import app.plugbrain.android.challenges.square.SquareRootChallengeTwoThroughTen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SquareRootChallengeTest {
    @Test
    fun `test square roots of perfect squares from 4 to 100`() {
        repeat(50) {
            val challenge = SquareRootChallengeTwoThroughTen()
            val answer = challenge.answer
            assertEquals(3, challenge.difficultyLevel)
            assertTrue(answer in 2..10, "$answer is not in 2..10")
            val radicand = answer * answer
            assertTrue(radicand in 4..100, "$radicand is not in 4..100")
            assertEquals("√$radicand = ?", challenge.string())
            assertTrue(challenge.checkAnswer(answer), "should accept $answer")
            assertFalse(challenge.checkAnswer(answer - 1), "should reject ${answer - 1}")
            assertFalse(challenge.checkAnswer(answer + 1), "should reject ${answer + 1}")
        }
    }

    @Test
    fun `test square roots of perfect squares from 121 to 400`() {
        repeat(50) {
            val challenge = SquareRootChallengeElevenThroughTwenty()
            val answer = challenge.answer
            assertEquals(7, challenge.difficultyLevel)
            assertTrue(answer in 11..20, "$answer is not in 11..20")
            val radicand = answer * answer
            assertTrue(radicand in 121..400, "$radicand is not in 121..400")
            assertEquals("√$radicand = ?", challenge.string())
            assertTrue(challenge.checkAnswer(answer), "should accept $answer")
            assertFalse(challenge.checkAnswer(answer - 1), "should reject ${answer - 1}")
            assertFalse(challenge.checkAnswer(answer + 1), "should reject ${answer + 1}")
        }
    }
}
