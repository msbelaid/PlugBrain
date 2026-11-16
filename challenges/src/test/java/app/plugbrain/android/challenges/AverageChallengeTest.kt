package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.average.AverageOfThreeTwoDigitNumbersChallenge
import app.plugbrain.android.challenges.average.AverageOfTwoTwoDigitNumbersChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AverageChallengeTest {
    @Test
    fun `test average of two 2-digit numbers`() {
        repeat(50) {
            val challenge = AverageOfTwoTwoDigitNumbersChallenge()
            val (first, second) = challenge.numbers
            assertEquals(8, challenge.difficultyLevel)
            assertEquals(2, challenge.numbers.size)
            assertTrue(first in 10..99, "$first is not in 10..99")
            assertTrue(second in 10..99, "$second is not in 10..99")
            assertEquals(
                first % 2,
                second % 2,
                "$first, $second: both numbers should be even or both odd",
            )
            val average = (first + second) / 2
            assertEquals(0, (first + second) % 2, "$first, $second: average is not whole")
            assertTrue(challenge.checkAnswer(average), "should accept $average")
            assertFalse(challenge.checkAnswer(average - 1), "should reject ${average - 1}")
            assertFalse(challenge.checkAnswer(average + 1), "should reject ${average + 1}")
            assertEquals("($first + $second) ÷ 2", challenge.string())
        }
    }

    @Test
    fun `test average of three 2-digit numbers`() {
        repeat(50) {
            val challenge = AverageOfThreeTwoDigitNumbersChallenge()
            val numbers = challenge.numbers
            assertEquals(11, challenge.difficultyLevel)
            assertEquals(3, numbers.size)
            numbers.forEach { number ->
                assertTrue(number in 10..99, "$number is not in 10..99")
            }
            val sum = numbers.sum()
            assertEquals(0, sum % 3, "$numbers: average is not whole")
            val average = sum / 3
            assertTrue(challenge.checkAnswer(average), "should accept $average")
            assertFalse(challenge.checkAnswer(average - 1), "should reject ${average - 1}")
            assertFalse(challenge.checkAnswer(average + 1), "should reject ${average + 1}")
            assertEquals("(${numbers.joinToString(" + ")}) ÷ 3", challenge.string())
        }
    }
}
