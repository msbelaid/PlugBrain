package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.challenges.square.SquareChallenge
import app.plugbrain.android.di.challengesModule
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.dsl.koinApplication

private const val SAMPLES = 500

// A minus sign stuck to a digit, as in "5 - -3". The subtraction sign has spaces around it.
private val NEGATIVE_NUMBER = Regex("-\\d")

/**
 * Checks every challenge the app can show, over many random draws: each one has exactly one
 * answer, the user can type it, and every difficulty level has something to show.
 */
class ChallengePropertiesTest {
    private val factory: ChallengeFactory = koinApplication { modules(challengesModule) }.koin.get()

    @Test
    fun `every level from 0 to the max has a challenge`() {
        val levels = factory.getChallenges().map { it.difficultyLevel }.toSet()

        assertEquals(0, factory.minDifficulty())
        assertEquals((0..factory.maxDifficulty()).toSet(), levels)
    }

    @Test
    fun `every difficulty preset is a level that exists`() {
        DifficultyPreset.entries.forEach { preset ->
            assertTrue("$preset", preset.minimalDifficulty in 0..factory.maxDifficulty())
        }
    }

    @Test
    fun `picking a challenge by level returns that level`() {
        (0..factory.maxDifficulty()).forEach { level ->
            repeat(20) {
                assertEquals(level, factory.getChallengeByDifficulty(level).difficultyLevel)
            }
        }
    }

    @Test
    fun `every challenge accepts its answer and nothing next to it`() {
        sampleEveryChallenge { challenge ->
            val answer = expectedAnswer(challenge)
            assertTrue("${challenge.string()} should accept $answer", challenge.checkAnswer(answer))
            assertFalse("${challenge.string()} should reject ${answer - 1}", challenge.checkAnswer(answer - 1))
            assertFalse("${challenge.string()} should reject ${answer + 1}", challenge.checkAnswer(answer + 1))
        }
    }

    @Test
    fun `answers can be typed on a number keyboard`() {
        // The answer field has no minus key on many keyboards, and parses the text as an Int.
        sampleEveryChallenge { challenge ->
            val answer = expectedAnswer(challenge)
            assertTrue("${challenge.string()} has a negative answer $answer", answer >= 0)
            assertEquals(answer, answer.toString().toIntOrNull())
        }
    }

    @Test
    fun `operands shown are never negative`() {
        sampleEveryChallenge { challenge ->
            assertFalse("${challenge.string()} shows a negative number", NEGATIVE_NUMBER.containsMatchIn(challenge.string()))
        }
    }

    @Test
    fun `a challenge type always has the same level`() {
        val levelByType = factory.getChallenges().associate { it::class to it.difficultyLevel }
        sampleEveryChallenge { challenge ->
            assertEquals(challenge::class.simpleName, levelByType[challenge::class], challenge.difficultyLevel)
        }
    }

    private fun sampleEveryChallenge(check: (NumericalChallenge) -> Unit) {
        repeat(SAMPLES) {
            factory.getChallenges().forEach { challenge ->
                val numerical = challenge as? NumericalChallenge
                    ?: error("${challenge::class.simpleName} can't be answered with a number")
                check(numerical)
            }
        }
    }

    /** The answer worked out from what the challenge shows, independently of its checkAnswer. */
    private fun expectedAnswer(challenge: NumericalChallenge): Int =
        when (challenge) {
            is TwoOperandsChallenge -> compute(challenge.operand1, challenge.operationType, challenge.operand2)
            is MissingChallenge -> {
                assertEquals(
                    challenge.string(),
                    challenge.result,
                    compute(challenge.operand1, challenge.operationType, challenge.operand2),
                )
                when (challenge.missingPosition) {
                    MissingPosition.FIRST_OPERAND -> challenge.operand1
                    MissingPosition.SECOND_OPERAND -> challenge.operand2
                    MissingPosition.RESULT -> challenge.result
                }
            }
            is SquareChallenge -> challenge.operand * challenge.operand
            else -> error("No expected answer for ${challenge::class.simpleName}")
        }

    private fun compute(operand1: Int, operator: Operator, operand2: Int): Int =
        when (operator) {
            Operator.ADDITION -> Math.addExact(operand1, operand2)
            Operator.SUBTRACTION -> Math.subtractExact(operand1, operand2)
            Operator.MULTIPLICATION -> Math.multiplyExact(operand1, operand2)
            Operator.DIVISION -> {
                assertEquals("$operand1 / $operand2 should divide exactly", 0, operand1 % operand2)
                operand1 / operand2
            }
        }
}
