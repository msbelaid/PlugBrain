package app.plugbrain.android.challenges.missing

import app.plugbrain.android.challenges.MissingChallenge
import app.plugbrain.android.challenges.MissingPosition
import app.plugbrain.android.challenges.Operator
import kotlin.random.Random

/**
 * Subtraction challenge with a missing number.
 * Examples:
 * - 15 - ? = 8 (answer: 7)
 * - ? - 5 = 10 (answer: 15)
 * - 20 - 7 = ? (answer: 13)
 */
class MissingSubtractionChallenge : MissingChallenge() {
  override val result: Int = Random.nextInt(0, 20)
  override val operand2: Int = Random.nextInt(1, 20)
  override val operand1: Int = result + operand2
  override val operationType: Operator = Operator.SUBTRACTION
  override val missingPosition: MissingPosition = MissingPosition.entries.random()

  override val difficultyLevel: Int = 2
}
