package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.substraction.SubtractFourDigitsChallenge
import app.plugbrain.android.challenges.substraction.SubtractFromSelfChallenge
import app.plugbrain.android.challenges.substraction.SubtractFromZeroChallenge
import app.plugbrain.android.challenges.substraction.SubtractOneDigitChallenge
import app.plugbrain.android.challenges.substraction.SubtractThreeDigitsChallenge
import app.plugbrain.android.challenges.substraction.SubtractThreeFromTwoDigitsChallenge
import app.plugbrain.android.challenges.substraction.SubtractTwoDigitsChallenge
import app.plugbrain.android.challenges.substraction.SubtractTwoDigitsNoBorrowChallenge
import app.plugbrain.android.challenges.substraction.SubtractTwoFromOneDigitChallenge
import app.plugbrain.android.challenges.substraction.SubtractUnderFiveChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SubtractChallengeTest {
    @Test
    fun `test subtract from self`() {
        repeat(10) {
            val challenge = SubtractFromSelfChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(1, challenge.difficultyLevel)
            assertTrue(a == b, "$a should equals $b")
            assertTrue(a in 1..9, "$a should be in 1..9")
            assertTrue(b in 1..9, "$b should be in 1..9")
        }
    }

    @Test
    fun `test subtract from zero`() {
        repeat(10) {
            val challenge = SubtractFromZeroChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(1, challenge.difficultyLevel)
            assertTrue(a in 1..9, "$a should be in 1..9")
            assertTrue(b == 0, "$b should be 0!")
        }
    }

    @Test
    fun `test subtract under five`() {
        repeat(10) {
            val challenge = SubtractUnderFiveChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(2, challenge.difficultyLevel)
            assertTrue(a >= b, "$a greater than $b")
            assertTrue(a in 1..5, "$a should be in 1..5")
            assertTrue(b in 1..5, "$b should be in 1..5")
        }
    }

    @Test
    fun `test subtract under ten`() {
        repeat(10) {
            val challenge = SubtractOneDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(3, challenge.difficultyLevel)
            assertTrue(a >= b, "$a greater than $b")
            assertTrue(a in 1..9, "$a should be in 1..9")
            assertTrue(b in 1..9, "$b should be in 1..9")
        }
    }

    @Test
    fun `test subtract two digits from one digit`() {
        repeat(10) {
            val challenge = SubtractTwoFromOneDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(4, challenge.difficultyLevel)
            assertTrue(a >= b, "$a greater than $b")
            assertTrue(a in 10..99, "$a should be in 10..99")
            assertTrue(b in 1..9, "$b should be in 1..9")
        }
    }

    @Test
    fun `test subtract two digits no borrow`() {
        repeat(10) {
            val challenge = SubtractTwoDigitsNoBorrowChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(5, challenge.difficultyLevel)
            assertTrue(a >= b, "$a is not greater than $b")
            assertTrue(a in 10..99, "$a is not in 10..99")
            assertTrue(b in 10..99, "$b is not in 10..99")
            assertTrue(a % 10 >= b % 10, "should be no borrow!!")
        }
    }

    @Test
    fun `test subtract two digits`() {
        repeat(10) {
            val challenge = SubtractTwoDigitsChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(6, challenge.difficultyLevel)
            assertTrue(a >= b, "$a is not greater than $b")
            assertTrue(a in 10..100, "$a is not in 10..100")
            assertTrue(b in 10..99, "$b is not in 10..99")
        }
    }

    @Test
    fun `test subtract three from two digits`() {
        repeat(10) {
            val challenge = SubtractThreeFromTwoDigitsChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(7, challenge.difficultyLevel)
            assertTrue(a >= b, "$a is not greater than $b")
            assertTrue(a in 100..1000, "$a is not in 10..100")
            assertTrue(b in 10..99, "$b is not in 10..99")
        }
    }

    @Test
    fun `test subtract three digits`() {
        repeat(10) {
            val challenge = SubtractThreeDigitsChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(8, challenge.difficultyLevel)
            assertTrue(a >= b, "$a is not greater than $b")
            assertTrue(a in 100..1000, "$a is not in 10..100")
            assertTrue(b in 100..1000, "$b is not in 10..99")
        }
    }

    @Test
    fun `test subtract four digits`() {
        repeat(50) {
            val challenge = SubtractFourDigitsChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(9, challenge.difficultyLevel)
            assertTrue(a >= b, "$a is not greater than $b")
            assertTrue(a in 1000..10000, "$a is not in 10..100")
            assertTrue(b in 1000..10000, "$b is not in 10..99")
            assertTrue(challenge.checkAnswer(a - b), "check answer is not correct")
            assertFalse(challenge.checkAnswer(a - b - 1), "check answer is not correct")
        }
    }
}
