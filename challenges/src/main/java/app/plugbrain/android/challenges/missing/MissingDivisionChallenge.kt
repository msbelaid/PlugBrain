package app.plugbrain.android.challenges.missing

import app.plugbrain.android.challenges.MissingChallenge
import app.plugbrain.android.challenges.MissingPosition
import app.plugbrain.android.challenges.Operator
import kotlin.random.Random

/**
 * Division challenge with a missing number.
 * Examples:
 * - 21 / ? = 3 (answer: 7)
 * - ? / 5 = 4 (answer: 20)
 * - 24 / 6 = ? (answer: 4)
 */
class MissingDivisionChallenge : MissingChallenge() {
  override val operand2: Int = Random.nextInt(2, 12)
  override val result: Int = Random.nextInt(2, 12)
  override val operand1: Int = operand2 * result
  override val operationType: Operator = Operator.DIVISION
  override val missingPosition: MissingPosition = MissingPosition.entries.random()

  override val difficultyLevel: Int = 4
}
