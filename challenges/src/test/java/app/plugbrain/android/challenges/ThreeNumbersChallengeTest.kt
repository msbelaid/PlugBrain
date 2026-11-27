package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.threenumbers.ThreeSingleDigitsPlusChallenge
import app.plugbrain.android.challenges.threenumbers.ThreeSingleDigitsPlusMinusChallenge
import app.plugbrain.android.challenges.threenumbers.ThreeTwoDigitsPlusMinusChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThreeNumbersChallengeTest {
    @Test
    fun `test three single digits, plus then minus`() {
        repeat(50) {
            val challenge = ThreeSingleDigitsPlusMinusChallenge()
            val (first, second, third) = challenge.numbers
            assertEquals(4, challenge.difficultyLevel)
            listOf(first, second, third).forEach { assertTrue(it in 1..9, "$it is not a single digit") }
            val total = first + second - third
            assertTrue(total >= 0, "${challenge.string()} goes below zero")
            assertTrue(challenge.checkAnswer(total), "should accept $total")
            assertFalse(challenge.checkAnswer(total - 1), "should reject ${total - 1}")
            assertFalse(challenge.checkAnswer(total + 1), "should reject ${total + 1}")
            assertEquals("$first + $second - $third", challenge.string())
        }
    }

    @Test
    fun `test three single digits, all plus`() {
        repeat(50) {
            val challenge = ThreeSingleDigitsPlusChallenge()
            val (first, second, third) = challenge.numbers
            assertEquals(4, challenge.difficultyLevel)
            listOf(first, second, third).forEach { assertTrue(it in 1..9, "$it is not a single digit") }
            val total = first + second + third
            assertTrue(challenge.checkAnswer(total), "should accept $total")
            assertFalse(challenge.checkAnswer(total - 1), "should reject ${total - 1}")
            assertFalse(challenge.checkAnswer(total + 1), "should reject ${total + 1}")
            assertEquals("$first + $second + $third", challenge.string())
        }
    }

    @Test
    fun `test three 2-digit numbers, plus then minus`() {
        repeat(50) {
            val challenge = ThreeTwoDigitsPlusMinusChallenge()
            val (first, second, third) = challenge.numbers
            assertEquals(8, challenge.difficultyLevel)
            listOf(first, second, third).forEach { assertTrue(it in 10..99, "$it is not in 10..99") }
            val total = first + second - third
            assertTrue(total >= 0, "$first + $second - $third goes below zero")
            assertTrue(challenge.checkAnswer(total), "should accept $total")
            assertFalse(challenge.checkAnswer(total - 1), "should reject ${total - 1}")
            assertFalse(challenge.checkAnswer(total + 1), "should reject ${total + 1}")
            assertEquals("$first + $second - $third", challenge.string())
        }
    }
}
