package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.doubles.DoubleOfTwoDigitChallenge
import app.plugbrain.android.challenges.halves.HalfOfEvenThreeDigitChallenge
import app.plugbrain.android.challenges.halves.HalfOfEvenTwoDigitChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HalvesAndDoublesChallengeTest {
    @Test
    fun `test half of an even two-digit number`() {
        repeat(50) {
            val challenge = HalfOfEvenTwoDigitChallenge()
            val n = challenge.operand1
            assertEquals(3, challenge.difficultyLevel)
            assertTrue(n in 10..98 && n % 2 == 0, "$n is not an even two-digit number")
            assertEquals(2, challenge.operand2)
            assertTrue(challenge.checkAnswer(n / 2), "should accept ${n / 2}")
            assertFalse(challenge.checkAnswer(n / 2 + 1), "should reject ${n / 2 + 1}")
            assertEquals("$n / 2", challenge.string())
        }
    }

    @Test
    fun `test half of an even three-digit number`() {
        repeat(50) {
            val challenge = HalfOfEvenThreeDigitChallenge()
            val n = challenge.operand1
            assertEquals(6, challenge.difficultyLevel)
            assertTrue(n in 100..998 && n % 2 == 0, "$n is not an even three-digit number")
            assertTrue(challenge.checkAnswer(n / 2), "should accept ${n / 2}")
            assertFalse(challenge.checkAnswer(n / 2 - 1), "should reject ${n / 2 - 1}")
        }
    }

    @Test
    fun `test double of a two-digit number`() {
        repeat(50) {
            val challenge = DoubleOfTwoDigitChallenge()
            val n = challenge.operand2
            assertEquals(4, challenge.difficultyLevel)
            assertEquals(2, challenge.operand1)
            assertTrue(n in 10..99, "$n is not a two-digit number")
            assertTrue(challenge.checkAnswer(2 * n), "should accept ${2 * n}")
            assertFalse(challenge.checkAnswer(2 * n + 1), "should reject ${2 * n + 1}")
            assertEquals("2 x $n", challenge.string())
        }
    }
}
