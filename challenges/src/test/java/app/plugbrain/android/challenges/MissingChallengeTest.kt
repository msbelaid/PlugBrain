package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.missing.MissingAdditionChallenge
import app.plugbrain.android.challenges.missing.MissingDivisionChallenge
import app.plugbrain.android.challenges.missing.MissingMultiplicationChallenge
import app.plugbrain.android.challenges.missing.MissingSubtractionChallenge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MissingChallengeTest {
    @Test
    fun `missing-number challenges display the result and hide one operand`() {
        repeat(30) {
            val challenges =
                listOf(
                    MissingAdditionChallenge(),
                    MissingSubtractionChallenge(),
                    MissingMultiplicationChallenge(),
                    MissingDivisionChallenge(),
                )
            for (challenge in challenges) {
                val equation = challenge.string()
                val parts = equation.split(" ")
                assertEquals(challenge.result.toString(), parts.last(), equation)
                assertEquals(1, parts.count { it == "?" }, equation)
                val answer = if (parts.first() == "?") challenge.operand1 else challenge.operand2
                assertTrue(challenge.checkAnswer(answer), equation)
                assertFalse(challenge.checkAnswer(answer + 1), equation)
            }
        }
    }

    @Test
    fun `test missing addition challenge generation`() {
        repeat(20) {
            val challenge = MissingAdditionChallenge()
            assertEquals(5, challenge.difficultyLevel)
            assertTrue(challenge.operand1 in 1..19, "${challenge.operand1} is not in 1..19")
            assertTrue(challenge.operand2 in 1..19, "${challenge.operand2} is not in 1..19")
            assertEquals(challenge.operand1 + challenge.operand2, challenge.result)
        }
    }

    @Test
    fun `test missing subtraction challenge generation`() {
        repeat(20) {
            val challenge = MissingSubtractionChallenge()
            assertEquals(5, challenge.difficultyLevel)
            assertTrue(challenge.result in 0..19, "${challenge.result} is not in 0..19")
            assertTrue(challenge.operand2 in 1..19, "${challenge.operand2} is not in 1..19")
            assertTrue(challenge.operand1 >= challenge.operand2, "operand1 should be >= operand2")
            assertEquals(challenge.operand1 - challenge.operand2, challenge.result)
        }
    }

    @Test
    fun `test missing subtraction challenge validation`() {
        repeat(10) {
            val challenge = MissingSubtractionChallenge()
            when (challenge.missingPosition) {
                MissingPosition.FIRST_OPERAND -> {
                    assertTrue(
                        challenge.checkAnswer(challenge.operand1),
                        "Should accept correct answer for first operand",
                    )
                    assertFalse(
                        challenge.checkAnswer(challenge.operand1 + 1),
                        "Should reject incorrect answer",
                    )
                }

                MissingPosition.SECOND_OPERAND -> {
                    assertTrue(
                        challenge.checkAnswer(challenge.operand2),
                        "Should accept correct answer for second operand",
                    )
                    assertFalse(
                        challenge.checkAnswer(challenge.operand2 + 1),
                        "Should reject incorrect answer",
                    )
                }
            }
        }
    }

    @Test
    fun `test missing multiplication challenge generation`() {
        repeat(20) {
            val challenge = MissingMultiplicationChallenge()
            assertEquals(6, challenge.difficultyLevel)
            assertTrue(challenge.operand1 in 2..11, "${challenge.operand1} is not in 2..11")
            assertTrue(challenge.operand2 in 2..11, "${challenge.operand2} is not in 2..11")
            assertEquals(challenge.operand1 * challenge.operand2, challenge.result)
        }
    }

    @Test
    fun `test missing multiplication challenge validation`() {
        repeat(10) {
            val challenge = MissingMultiplicationChallenge()
            when (challenge.missingPosition) {
                MissingPosition.FIRST_OPERAND -> {
                    assertTrue(
                        challenge.checkAnswer(challenge.operand1),
                        "Should accept correct answer for first operand",
                    )
                    assertFalse(
                        challenge.checkAnswer(challenge.operand1 + 1),
                        "Should reject incorrect answer",
                    )
                }

                MissingPosition.SECOND_OPERAND -> {
                    assertTrue(
                        challenge.checkAnswer(challenge.operand2),
                        "Should accept correct answer for second operand",
                    )
                    assertFalse(
                        challenge.checkAnswer(challenge.operand2 + 1),
                        "Should reject incorrect answer",
                    )
                }
            }
        }
    }

    @Test
    fun `test missing division challenge generation`() {
        repeat(20) {
            val challenge = MissingDivisionChallenge()
            assertEquals(7, challenge.difficultyLevel)
            assertTrue(challenge.operand2 in 2..11, "${challenge.operand2} is not in 2..11")
            assertTrue(challenge.result in 2..11, "${challenge.result} is not in 2..11")
            assertEquals(
                challenge.operand2 * challenge.result,
                challenge.operand1,
                "Division should be exact",
            )
            assertEquals(challenge.operand1 / challenge.operand2, challenge.result)
        }
    }

    @Test
    fun `test missing division challenge validation`() {
        repeat(10) {
            val challenge = MissingDivisionChallenge()
            when (challenge.missingPosition) {
                MissingPosition.FIRST_OPERAND -> {
                    assertTrue(
                        challenge.checkAnswer(challenge.operand1),
                        "Should accept correct answer for first operand",
                    )
                    assertFalse(
                        challenge.checkAnswer(challenge.operand1 + 1),
                        "Should reject incorrect answer",
                    )
                }

                MissingPosition.SECOND_OPERAND -> {
                    assertTrue(
                        challenge.checkAnswer(challenge.operand2),
                        "Should accept correct answer for second operand",
                    )
                    assertFalse(
                        challenge.checkAnswer(challenge.operand2 + 1),
                        "Should reject incorrect answer",
                    )
                }
            }
        }
    }

    @Test
    fun `test missing challenge string representation`() {
        // Test all three positions for addition
        repeat(10) {
            val challenge = MissingAdditionChallenge()
            val challengeString = challenge.string()

            when (challenge.missingPosition) {
                MissingPosition.FIRST_OPERAND -> {
                    assertTrue(
                        challengeString.startsWith("?"),
                        "Should start with '?' for missing first operand",
                    )
                    assertTrue(
                        challengeString.contains("+ ${challenge.operand2}"),
                        "Should contain second operand",
                    )
                    assertTrue(
                        challengeString.endsWith("= ${challenge.result}"),
                        "Should end with result",
                    )
                }

                MissingPosition.SECOND_OPERAND -> {
                    assertTrue(
                        challengeString.startsWith("${challenge.operand1}"),
                        "Should start with first operand",
                    )
                    assertTrue(
                        challengeString.contains("+ ?"),
                        "Should contain '?' for second operand",
                    )
                    assertTrue(
                        challengeString.endsWith("= ${challenge.result}"),
                        "Should end with result",
                    )
                }
            }
        }
    }

    @Test
    fun `test all operations have correct symbols`() {
        val addition = MissingAdditionChallenge()
        assertTrue(addition.string().contains("+"), "Addition should use '+' symbol")

        val subtraction = MissingSubtractionChallenge()
        assertTrue(subtraction.string().contains("-"), "Subtraction should use '-' symbol")

        val multiplication = MissingMultiplicationChallenge()
        assertTrue(multiplication.string().contains("x"), "Multiplication should use 'x' symbol")

        val division = MissingDivisionChallenge()
        assertTrue(division.string().contains("/"), "Division should use '/' symbol")
    }
}
