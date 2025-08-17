package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.multiplication.MultiplicationByElevenChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationByFiveChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationByTenChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationByTwoChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationByZeroOrOneChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationDoubleByDoubleDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationFourByFourDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleByDoubleDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleByThreeDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleByThreeMultipleTenDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleDigitByMultipleTenChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationThreeByFourDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationThreeByThreeDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationTwoByThreeDigitChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MultiplicationChallengeTest {
    @Test
    fun `test multiplication by zero or one`() {
        repeat(10) {
            val challenge = MultiplicationByZeroOrOneChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(1, challenge.difficultyLevel)
            assertTrue(a in 0..1, "$a is not in 0..1")
            assertTrue(b in 2..10, "$b is not in 2..10")
        }
    }

    @Test
    fun `test multiplication by ten`() {
        repeat(10) {
            val challenge = MultiplicationByTenChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(2, challenge.difficultyLevel)
            assertTrue(a == 10, "$a is not 10")
            assertTrue(b in 2..10, "$b is not in 2..10")
        }
    }

    @Test
    fun `test multiplication by eleven`() {
        repeat(10) {
            val challenge = MultiplicationByElevenChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(3, challenge.difficultyLevel)
            assertTrue(a == 11, "$a is not 11")
            assertTrue(b in 2..10, "$b is not in 2..10")
        }
    }

    @Test
    fun `test multiplication by two`() {
        repeat(10) {
            val challenge = MultiplicationByTwoChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(4, challenge.difficultyLevel)
            assertTrue(a == 2, "$a is not 2")
            assertTrue(b in 2..10, "$b is not in 2..10")
        }
    }

    @Test
    fun `test multiplication by five`() {
        repeat(10) {
            val challenge = MultiplicationByFiveChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(5, challenge.difficultyLevel)
            assertTrue(a == 5, "$a is not 5")
            assertTrue(b in 2..10, "$b is not in 2..10")
        }
    }

    @Test
    fun `test multiplication single digit`() {
        repeat(10) {
            val challenge = MultiplicationSingleDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(6, challenge.difficultyLevel)
            assertTrue(a in setOf(3, 4, 6, 7, 8, 9), "$a is not in 3..10 except 5")
            assertTrue(b in 2..10, "$b is not in 2..10")
        }
    }

    @Test
    fun `test multiplication single digit by multiple ten`() {
        repeat(10) {
            val challenge = MultiplicationSingleDigitByMultipleTenChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(7, challenge.difficultyLevel)
            assertTrue(a in 3..9, "$a is not 3..9")
            assertTrue(b in 20..90, "$b is not in 20..90")
            assertTrue(b % 10 == 0, "$b is not multiple of 10")
        }
    }

    @Test
    fun `test multiplication single digit by double digit`() {
        repeat(10) {
            val challenge = MultiplicationSingleByDoubleDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(8, challenge.difficultyLevel)
            assertTrue(a in 3..9, "$a is not 3..9")
            assertTrue(b in 11..99, "$b is not in 11..99")
        }
    }

    @Test
    fun `test multiplication single digit by three digits multiple ten`() {
        repeat(10) {
            val challenge = MultiplicationSingleByThreeMultipleTenDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(9, challenge.difficultyLevel)
            assertTrue(a in 3..9, "$a is not 3..9")
            assertTrue(b in 110..990, "$b is not in 110..990")
            assertTrue(b % 10 == 0, "$b is not multiple of 10")
        }
    }

    @Test
    fun `test multiplication single digit by three digits`() {
        repeat(10) {
            val challenge = MultiplicationSingleByThreeDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(10, challenge.difficultyLevel)
            assertTrue(a in 3..9, "$a is not 3..9")
            assertTrue(b in 101..999, "$b is not in 101..999")
        }
    }

    @Test
    fun `test multiplication double by double digit`() {
        repeat(10) {
            val challenge = MultiplicationDoubleByDoubleDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(11, challenge.difficultyLevel)
            assertTrue(a in 11..99, "$a is not in 11..99")
            assertTrue(b in 11..99, "$b is not in 11..99")
        }
    }

    @Test
    fun `test multiplication double by three digits`() {
        repeat(10) {
            val challenge = MultiplicationTwoByThreeDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(12, challenge.difficultyLevel)
            assertTrue(a in 11..99, "$a is not in 11..99")
            assertTrue(b in 101..999, "$b is not in 101..999")
        }
    }

    @Test
    fun `test multiplication three by three digits`() {
        repeat(10) {
            val challenge = MultiplicationThreeByThreeDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(13, challenge.difficultyLevel)
            assertTrue(a in 101..999, "$a is not in 101..999")
            assertTrue(b in 101..999, "$b is not in 101..999")
        }
    }

    @Test
    fun `test multiplication three by four digits`() {
        repeat(10) {
            val challenge = MultiplicationThreeByFourDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(14, challenge.difficultyLevel)
            assertTrue(a in 101..999, "$a is not in 101..999")
            assertTrue(b in 1001..9999, "$b is not in 1001..9999")
        }
    }

    @Test
    fun `test multiplication four by four digits`() {
        repeat(10) {
            val challenge = MultiplicationFourByFourDigitChallenge()
            val a = challenge.operand1
            val b = challenge.operand2
            assertEquals(15, challenge.difficultyLevel)
            assertTrue(a in 1001..9999, "$a is not in 1001..9999")
            assertTrue(b in 1001..9999, "$b is not in 1001..9999")
            assertTrue(challenge.checkAnswer(a * b), "check answer is not correct")
            assertFalse(challenge.checkAnswer(a * b - 1), "check answer is not correct")
        }
    }
}
