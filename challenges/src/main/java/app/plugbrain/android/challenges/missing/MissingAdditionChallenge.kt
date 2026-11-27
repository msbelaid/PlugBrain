package app.plugbrain.android.challenges.missing

import app.plugbrain.android.challenges.MissingChallenge
import app.plugbrain.android.challenges.Operator
import kotlin.random.Random

/**
 * Addition challenge with a missing number.
 * Examples:
 * - 17 + ? = 25 (answer: 8)
 * - ? + 5 = 12 (answer: 7)
 */
class MissingAdditionChallenge : MissingChallenge() {
  override val operand1: Int = Random.nextInt(1, 20)
  override val operand2: Int = Random.nextInt(1, 20)
  override val result: Int = operand1 + operand2
  override val operationType: Operator = Operator.ADDITION

  override val difficultyLevel: Int = 5
}
