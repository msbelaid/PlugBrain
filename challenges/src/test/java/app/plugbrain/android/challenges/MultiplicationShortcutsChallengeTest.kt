package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.multiplication.MultiplicationSingleDigitByNineChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleDigitByNinetyNineChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleDigitByNumberEndingInNineChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationTwoDigitByElevenChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationTwoDigitByElevenToNineteenChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MultiplicationShortcutsChallengeTest {
    @Test
    fun `test single digit by nine`() {
        repeat(10) {
            val challenge = MultiplicationSingleDigitByNineChallenge()
            assertEquals(5, challenge.difficultyLevel)
            assertEquals(9, challenge.operand1)
            assertTrue(challenge.operand2 in 2..9, "${challenge.operand2} is not in 2..9")
            assertTrue(challenge.checkAnswer(challenge.operand1 * challenge.operand2))
        }
    }

    @Test
    fun `test two digit by eleven`() {
        repeat(10) {
            val challenge = MultiplicationTwoDigitByElevenChallenge()
            assertEquals(7, challenge.difficultyLevel)
            assertEquals(11, challenge.operand1)
            assertTrue(challenge.operand2 in 10..99, "${challenge.operand2} is not in 10..99")
            assertTrue(challenge.checkAnswer(challenge.operand1 * challenge.operand2))
        }
    }

    @Test
    fun `test single digit by ninety nine`() {
        repeat(10) {
            val challenge = MultiplicationSingleDigitByNinetyNineChallenge()
            assertEquals(7, challenge.difficultyLevel)
            assertEquals(99, challenge.operand2)
            assertTrue(challenge.operand1 in 2..9, "${challenge.operand1} is not in 2..9")
            assertTrue(challenge.checkAnswer(challenge.operand1 * challenge.operand2))
        }
    }

    @Test
    fun `test single digit by number ending in nine`() {
        repeat(20) {
            val challenge = MultiplicationSingleDigitByNumberEndingInNineChallenge()
            assertEquals(8, challenge.difficultyLevel)
            assertTrue(challenge.operand1 in 2..9, "${challenge.operand1} is not in 2..9")
            assertTrue(challenge.operand2 % 10 == 9, "${challenge.operand2} does not end in 9")
            assertTrue(challenge.operand2 in 19..99, "${challenge.operand2} is not in 19..99")
            assertTrue(challenge.checkAnswer(challenge.operand1 * challenge.operand2))
        }
    }

    @Test
    fun `test two digit by eleven to nineteen`() {
        repeat(10) {
            val challenge = MultiplicationTwoDigitByElevenToNineteenChallenge()
            assertEquals(11, challenge.difficultyLevel)
            assertTrue(challenge.operand1 in 11..19, "${challenge.operand1} is not in 11..19")
            assertTrue(challenge.operand2 in 10..99, "${challenge.operand2} is not in 10..99")
            assertTrue(challenge.checkAnswer(challenge.operand1 * challenge.operand2))
        }
    }
}
