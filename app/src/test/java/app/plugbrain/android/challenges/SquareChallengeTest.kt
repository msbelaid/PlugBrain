package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.square.SquareChallengeEasyTwoThroughFive
import app.plugbrain.android.challenges.square.SquareChallengeMediumSixThroughTen
import app.plugbrain.android.challenges.square.SquareChallengeHardElevenThroughTwenty
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
            assertEquals(1, challenge.difficultyLevel)
            assertTrue(a in 2..5, "$a is in 2..5")
        }
    }

    @Test
    fun `test if operand in medium challenge is correct`() {
        repeat(10) {
            val challenge = SquareChallengeMediumSixThroughTen()
            val a = challenge.operand
            assertEquals(2, challenge.difficultyLevel)
            assertTrue(a in 6..10, "$a is in 6..10")
        }
    }

    @Test
    fun `test if operand in hard challenge is correct `() {
        repeat(10) {
            val challenge = SquareChallengeHardElevenThroughTwenty()
            val a = challenge.operand
            assertEquals(3, challenge.difficultyLevel)
            assertTrue(a in 11..20, "$a is not in 30..99")
        }
    }

    @Test
    fun `test if operand and result are correct in easy challenge`() {
        repeat(10) {
            val challenge = SquareChallengeEasyTwoThroughFive()
            val a = challenge.operand
            assertEquals(1, challenge.difficultyLevel)
            assertTrue(a in 2..5, "$a is not in 10..99")
            assertTrue(challenge.checkAnswer(a * a), "check answer is correct")
            assertFalse(challenge.checkAnswer(a * a - 1), "check answer is not correct")
        }
    }

    @Test
    fun `test if operand and result are correct in medium challenge`() {
        repeat(10) {
            val challenge = SquareChallengeMediumSixThroughTen()
            val a = challenge.operand
            assertEquals(2, challenge.difficultyLevel)
            assertTrue(a in 6..10, "$a is not in 11..99")
            assertTrue(challenge.checkAnswer(a * a), "check answer is correct")
            assertFalse(challenge.checkAnswer(a * a - 1), "check answer is not correct")
        }
    }

    @Test
    fun `test if operand and result are correct in hard challenge`() {
        repeat(10) {
            val challenge = SquareChallengeHardElevenThroughTwenty()
            val a = challenge.operand
            assertEquals(3, challenge.difficultyLevel)
            assertTrue(a in 11..20, "$a is not in 30..99")
            assertTrue(challenge.checkAnswer(a * a), "check answer is correct")
            assertFalse(challenge.checkAnswer(a * a - 1), "check answer is not correct")
        }
    }
}