package app.plugbrain.android.challenges.missing

import app.plugbrain.android.challenges.MissingChallenge
import app.plugbrain.android.challenges.MissingPosition
import app.plugbrain.android.challenges.Operator
import kotlin.random.Random

/**
 * Multiplication challenge with a missing number.
 * Examples:
 * - 7 x ? = 28 (answer: 4)
 * - ? x 5 = 35 (answer: 7)
 * - 6 x 8 = ? (answer: 48)
 */
class MissingMultiplicationChallenge : MissingChallenge() {
  override val operand1: Int = Random.nextInt(2, 12)
  override val operand2: Int = Random.nextInt(2, 12)
  override val result: Int = operand1 * operand2
  override val operationType: Operator = Operator.MULTIPLICATION
  override val missingPosition: MissingPosition = MissingPosition.entries.random()

  override val difficultyLevel: Int = 3
}
