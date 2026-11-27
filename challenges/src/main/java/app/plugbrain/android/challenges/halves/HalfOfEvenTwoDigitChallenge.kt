package app.plugbrain.android.challenges.halves

import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.Operator
import app.plugbrain.android.challenges.TwoOperandsChallenge

/**
 * Half of an even two-digit number. Example: 74 / 2
 */
class HalfOfEvenTwoDigitChallenge : TwoOperandsChallenge, NumericalChallenge {
  override val operand1: Int = (5..49).random() * 2
  override val operand2: Int = 2
  override val operationType: Operator = Operator.DIVISION
  override val difficultyLevel: Int = 3

  override fun checkAnswer(response: Int): Boolean {
    return response == operand1 / operand2
  }
}
