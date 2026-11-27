package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.mixed.MixedProductMinusSingleDigitChallenge
import app.plugbrain.android.challenges.mixed.MixedProductPlusSingleDigitChallenge
import app.plugbrain.android.challenges.mixed.MixedSumInBracketsTimesSingleDigitChallenge
import app.plugbrain.android.challenges.mixed.MixedSumInBracketsTimesTwoDigitChallenge
import app.plugbrain.android.challenges.mixed.MixedTwoProductsAddedChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MixedOperationsChallengeTest {
    @Test
    fun `test product plus single digit`() {
        repeat(100) {
            val challenge = MixedProductPlusSingleDigitChallenge()
            assertEquals(7, challenge.difficultyLevel)
            assertTrue(challenge.factor1 in 2..9)
            assertTrue(challenge.factor2 in 2..9)
            assertTrue(challenge.addend in 1..9)
            assertTrue(challenge.checkAnswer(challenge.factor1 * challenge.factor2 + challenge.addend))
            assertFalse(challenge.checkAnswer(challenge.factor1 * challenge.factor2 - challenge.addend))
            assertTrue(challenge.string().contains("x"))
        }
    }

    @Test
    fun `test product minus single digit never gives a negative answer`() {
        repeat(100) {
            val challenge = MixedProductMinusSingleDigitChallenge()
            val product = challenge.factor1 * challenge.factor2
            assertEquals(7, challenge.difficultyLevel)
            assertTrue(challenge.subtrahend in 1..9)
            assertTrue(challenge.subtrahend <= product, "${challenge.subtrahend} is larger than $product")
            assertTrue(challenge.checkAnswer(product - challenge.subtrahend))
            assertFalse(challenge.checkAnswer(product + challenge.subtrahend))
        }
    }

    @Test
    fun `test sum in brackets times single digit`() {
        repeat(100) {
            val challenge = MixedSumInBracketsTimesSingleDigitChallenge()
            assertEquals(9, challenge.difficultyLevel)
            assertTrue(challenge.addend1 in 10..19)
            assertTrue(challenge.addend2 in 2..9)
            assertTrue(challenge.factor in 2..9)
            assertTrue(challenge.checkAnswer((challenge.addend1 + challenge.addend2) * challenge.factor))
            assertTrue(challenge.string().startsWith("("))
        }
    }

    @Test
    fun `test two products added`() {
        repeat(100) {
            val challenge = MixedTwoProductsAddedChallenge()
            assertEquals(8, challenge.difficultyLevel)
            assertTrue(challenge.checkAnswer(challenge.first1 * challenge.first2 + challenge.second1 * challenge.second2))
            assertFalse(challenge.checkAnswer(challenge.first1 * challenge.first2 * challenge.second1 * challenge.second2))
        }
    }

    @Test
    fun `test sum in brackets times two digit number`() {
        repeat(100) {
            val challenge = MixedSumInBracketsTimesTwoDigitChallenge()
            assertEquals(13, challenge.difficultyLevel)
            assertTrue(challenge.addend1 in 10..99)
            assertTrue(challenge.addend2 in 10..99)
            assertTrue(challenge.factor in 10..99)
            assertTrue(challenge.checkAnswer((challenge.addend1 + challenge.addend2) * challenge.factor))
            assertEquals("(${challenge.addend1} + ${challenge.addend2}) x ${challenge.factor}", challenge.string())
        }
    }
}
