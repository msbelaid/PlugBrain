package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.percentage.PercentageChallenge
import app.plugbrain.android.challenges.percentage.PercentageFifteenOfMultipleOfTwentyChallenge
import app.plugbrain.android.challenges.percentage.PercentageQuarterOfMultipleOfFourChallenge
import app.plugbrain.android.challenges.percentage.PercentageTenOrFiftyOfMultipleOfTenChallenge
import app.plugbrain.android.challenges.percentage.PercentageTwentyOrFiveOfMultipleOfTwentyChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PercentageChallengeTest {
    @Test
    fun `test ten or fifty percent of a multiple of ten`() {
        repeat(50) {
            val challenge = PercentageTenOrFiftyOfMultipleOfTenChallenge()
            assertEquals(7, challenge.difficultyLevel)
            assertTrue(challenge.percent == 10 || challenge.percent == 50, "${challenge.percent}")
            assertEquals(0, challenge.base % 10)
            assertWholeAnswer(challenge)
        }
    }

    @Test
    fun `test a quarter of a multiple of four`() {
        repeat(50) {
            val challenge = PercentageQuarterOfMultipleOfFourChallenge()
            assertEquals(9, challenge.difficultyLevel)
            assertEquals(25, challenge.percent)
            assertEquals(0, challenge.base % 4)
            assertWholeAnswer(challenge)
        }
    }

    @Test
    fun `test twenty or five percent of a multiple of twenty`() {
        repeat(50) {
            val challenge = PercentageTwentyOrFiveOfMultipleOfTwentyChallenge()
            assertEquals(9, challenge.difficultyLevel)
            assertTrue(challenge.percent == 20 || challenge.percent == 5, "${challenge.percent}")
            assertEquals(0, challenge.base % 20)
            assertWholeAnswer(challenge)
        }
    }

    @Test
    fun `test fifteen percent of a multiple of twenty`() {
        repeat(50) {
            val challenge = PercentageFifteenOfMultipleOfTwentyChallenge()
            assertEquals(12, challenge.difficultyLevel)
            assertEquals(15, challenge.percent)
            assertEquals(0, challenge.base % 20)
            assertWholeAnswer(challenge)
        }
    }

    @Test
    fun `test percentage shows symbols only and accepts the whole answer`() {
        repeat(50) {
            val challenge = PercentageFifteenOfMultipleOfTwentyChallenge()
            val answer = challenge.percent * challenge.base / 100
            assertEquals("15% x ${challenge.base}", challenge.string())
            assertTrue(challenge.checkAnswer(answer), "should accept $answer")
            assertFalse(challenge.checkAnswer(answer - 1), "should reject ${answer - 1}")
            assertFalse(challenge.checkAnswer(answer + 1), "should reject ${answer + 1}")
        }
    }

    private fun assertWholeAnswer(challenge: PercentageChallenge) {
        val product = challenge.percent * challenge.base
        assertEquals(0, product % 100, "${challenge.string()} should have a whole answer")
        val answer = product / 100
        assertTrue(challenge.checkAnswer(answer), "${challenge.string()} should accept $answer")
    }
}
