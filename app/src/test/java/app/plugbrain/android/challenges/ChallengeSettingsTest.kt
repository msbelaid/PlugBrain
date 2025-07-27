package app.plugbrain.android.challenges

import app.plugbrain.android.repository.model.ChallengeSettings
import app.plugbrain.android.repository.model.Difficulty
import app.plugbrain.android.repository.model.Operator
import kotlin.test.Test
import kotlin.test.assertTrue

class ChallengeSettingsTest {
    @Test
    fun testAdditionBeginnerChallengeRange() {
        repeat(10) {
            val challenge = ChallengeSettings(Operator.ADDITION, Difficulty.BEGINNER).generate()
            val a = challenge.num1
            val b = challenge.num2
            assertTrue(a in 1..10, "$a should be in 1..9")
            assertTrue(b in 1..10, "$b should be in 1..9")
        }
    }

    @Test
    fun testAdditionEasyChallengeRange() {
        repeat(5) {
            val challenge = ChallengeSettings(Operator.ADDITION, Difficulty.EASY).generate()
            val a = challenge.num1
            val b = challenge.num2
            val a1 = a / 10
            val a0 = a % 10
            val b1 = b / 10
            val b0 = b % 10
            assertTrue(a0 + b0 in 0..9, "no remainder, first digit")
            assertTrue(a1 + b1 in 0..9, "no remainder, second digit")
            assertTrue(a in 10..99, "$a should have two digits")
            assertTrue(b in 10..99, "$b should have two digits")
        }
    }
}
