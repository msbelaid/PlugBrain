package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.addition.AdditionFiveDigitsChallenge
import app.plugbrain.android.challenges.addition.AdditionFourDigitsChallenge
import app.plugbrain.android.challenges.addition.AdditionOneAndTwoDigitsChallenge
import app.plugbrain.android.challenges.addition.AdditionPlusZeroOrOneChallenge
import app.plugbrain.android.challenges.addition.AdditionThreeDigitsChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsCarryFreeChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsWithCarryChallenge
import app.plugbrain.android.challenges.addition.AdditionUnderFiveChallenge
import app.plugbrain.android.challenges.addition.AdditionUnderTenChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdditionChallengeTest {
    @Test
    fun `test plus zero or one`() {
        repeat(10) {
            val challenge = AdditionPlusZeroOrOneChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(0, challenge.difficultyLevel)
            assertTrue(a in 1..9, "$a is not in 1..9")
            assertTrue(b in 0..1, "$b is not in {0, 1}")
        }
    }

    @Test
    fun `test addition under five`() {
        repeat(10) {
            val challenge = AdditionUnderFiveChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(1, challenge.difficultyLevel)
            assertTrue(a in 2..5, "$a is not in 2..5")
            assertTrue(b in 2..5, "$b is not in 2..5")
        }
    }

    @Test
    fun `test addition under ten`() {
        repeat(10) {
            val challenge = AdditionUnderTenChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(2, challenge.difficultyLevel)
            assertTrue(a in 2..9, "$a is not in 2..9")
            assertTrue(b in 2..9, "$b is not in 2..9")
        }
    }

    @Test
    fun `test addition two + one digit `() {
        repeat(10) {
            val challenge = AdditionOneAndTwoDigitsChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(3, challenge.difficultyLevel)
            assertTrue(a in 10..99, "$a is not in 10..99")
            assertTrue(b in 2..9, "$b is not in 2..9")
        }
    }

    @Test
    fun `test addition two digits carry free`() {
        repeat(10) {
            val challenge = AdditionTwoDigitsCarryFreeChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(4, challenge.difficultyLevel)
            assertTrue(a in 10..99, "$a is not in 10..99")
            assertTrue(b in 10..99, "$b is not in 1..9")
            assertTrue(a % 10 + b % 10 < 10, "$b, $a: should not be more than 9")
            assertTrue(a / 10 + b / 10 < 10, "$b, $a: should not be more than 9")
        }
    }

    @Test
    fun `test addition two digits with carry`() {
        repeat(10) {
            val challenge = AdditionTwoDigitsWithCarryChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(5, challenge.difficultyLevel)
            assertTrue(a in 10..99, "$a is not in 10..99")
            assertTrue(b in 10..99, "$b is not in 1..9")
            assertTrue(a % 10 + b % 10 >= 10, "$b, $a: should not be more than 9")
        }
    }

    @Test
    fun `test addition three digits`() {
        repeat(20) {
            val challenge = AdditionThreeDigitsChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(6, challenge.difficultyLevel)
            assertTrue(a in 100..999, "$a is not in 100..999")
            assertTrue(b in 100..999, "$b is not in 100..999")
        }
    }

    @Test
    fun `test addition four digits`() {
        repeat(50) {
            val challenge = AdditionFourDigitsChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(8, challenge.difficultyLevel)
            assertTrue(a in 1000..9999, "$a is not in 100..999")
            assertTrue(b in 1000..9999, "$b is not in 100..999")
        }
    }

    @Test
    fun `test addition five digits`() {
        repeat(100) {
            val challenge = AdditionFiveDigitsChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(10, challenge.difficultyLevel)
            assertTrue(a in 10000..99999, "$a is not in 10000..99999")
            assertTrue(b in 10000..99999, "$b is not in 10000..99999")
            assertTrue(challenge.checkAnswer(a + b), "check answer is not correct")
            assertFalse(challenge.checkAnswer(a + b - 1), "check answer is not correct")
        }
    }
}
