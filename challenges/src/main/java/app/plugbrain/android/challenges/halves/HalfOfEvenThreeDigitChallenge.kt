package app.plugbrain.android.challenges.halves

import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.Operator
import app.plugbrain.android.challenges.TwoOperandsChallenge

/**
 * Half of an even three-digit number. Example: 358 / 2
 */
class HalfOfEvenThreeDigitChallenge : TwoOperandsChallenge, NumericalChallenge {
  override val operand1: Int = (50..499).random() * 2
  override val operand2: Int = 2
  override val operationType: Operator = Operator.DIVISION
  override val difficultyLevel: Int = 6

  override fun checkAnswer(response: Int): Boolean {
    return response == operand1 / operand2
  }
}
