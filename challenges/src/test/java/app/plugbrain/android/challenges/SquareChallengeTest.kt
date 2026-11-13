package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.square.SquareChallengeEasyTwoThroughFive
import app.plugbrain.android.challenges.square.SquareChallengeEndingInFiveFifteenThroughNinetyFive
import app.plugbrain.android.challenges.square.SquareChallengeFiftyOneThroughNinetyNine
import app.plugbrain.android.challenges.square.SquareChallengeHardElevenThroughTwenty
import app.plugbrain.android.challenges.square.SquareChallengeMediumSixThroughTen
import app.plugbrain.android.challenges.square.SquareChallengeMultipleOfTenTwentyThroughNinety
import app.plugbrain.android.challenges.square.SquareChallengeTwentyOneThroughFifty
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SquareChallengeTest {
    @Test
    fun `test if operand in easy challenge is correct`() {
        repeat(10) {
            val challenge = SquareChallengeEasyTwoThroughFive()
            val a = challenge.operand
            assertEquals(2, challenge.difficultyLevel)
            assertTrue(a in 2..5, "$a is in 2..5")
        }
    }

    @Test
    fun `test if operand in medium challenge is correct`() {
        repeat(10) {
            val challenge = SquareChallengeMediumSixThroughTen()
            val a = challenge.operand
            assertEquals(4, challenge.difficultyLevel)
            assertTrue(a in 6..10, "$a is in 6..10")
        }
    }

    @Test
    fun `test if operand in hard challenge is correct `() {
        repeat(10) {
            val challenge = SquareChallengeHardElevenThroughTwenty()
            val a = challenge.operand
            assertEquals(8, challenge.difficultyLevel)
            assertTrue(a in 11..20, "$a is not in 11..20")
        }
    }

    @Test
    fun `test if operand and result are correct in easy challenge`() {
        repeat(10) {
            val challenge = SquareChallengeEasyTwoThroughFive()
            val a = challenge.operand
            assertEquals(2, challenge.difficultyLevel)
            assertTrue(a in 2..5, "$a is not in 2..5")
            assertTrue(challenge.checkAnswer(a * a), "check answer is correct")
            assertFalse(challenge.checkAnswer(a * a - 1), "check answer is not correct")
        }
    }

    @Test
    fun `test if operand and result are correct in medium challenge`() {
        repeat(10) {
            val challenge = SquareChallengeMediumSixThroughTen()
            val a = challenge.operand
            assertEquals(4, challenge.difficultyLevel)
            assertTrue(a in 6..10, "$a is not in 6..10")
            assertTrue(challenge.checkAnswer(a * a), "check answer is correct")
            assertFalse(challenge.checkAnswer(a * a - 1), "check answer is not correct")
        }
    }

    @Test
    fun `test if operand and result are correct in hard challenge`() {
        repeat(10) {
            val challenge = SquareChallengeHardElevenThroughTwenty()
            val a = challenge.operand
            assertEquals(8, challenge.difficultyLevel)
            assertTrue(a in 11..20, "$a is not in 11..20")
            assertTrue(challenge.checkAnswer(a * a), "check answer is correct")
            assertFalse(challenge.checkAnswer(a * a - 1), "check answer is not correct")
        }
    }

    @Test
    fun `test operands and levels of the bigger square challenges`() {
        repeat(50) {
            val multipleOfTen = SquareChallengeMultipleOfTenTwentyThroughNinety()
            assertEquals(5, multipleOfTen.difficultyLevel)
            assertTrue(multipleOfTen.operand in 20..90 && multipleOfTen.operand % 10 == 0, "${multipleOfTen.operand}")

            val endingInFive = SquareChallengeEndingInFiveFifteenThroughNinetyFive()
            assertEquals(9, endingInFive.difficultyLevel)
            assertTrue(endingInFive.operand in 15..95 && endingInFive.operand % 10 == 5, "${endingInFive.operand}")

            val twentyOne = SquareChallengeTwentyOneThroughFifty()
            assertEquals(12, twentyOne.difficultyLevel)
            assertTrue(twentyOne.operand in 21..50, "${twentyOne.operand}")

            val fiftyOne = SquareChallengeFiftyOneThroughNinetyNine()
            assertEquals(13, fiftyOne.difficultyLevel)
            assertTrue(fiftyOne.operand in 51..99, "${fiftyOne.operand}")

            listOf(multipleOfTen, endingInFive, twentyOne, fiftyOne).forEach { challenge ->
                assertTrue(challenge.checkAnswer(challenge.operand * challenge.operand))
                assertFalse(challenge.checkAnswer(challenge.operand * challenge.operand + 1))
            }
        }
    }
}
