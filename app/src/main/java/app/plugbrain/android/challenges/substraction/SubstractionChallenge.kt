package app.plugbrain.android.challenges.substraction

import app.plugbrain.android.challenges.Operator
import app.plugbrain.android.challenges.TwoOperandsChallenge

interface SubstractionChallenge : TwoOperandsChallenge {
  override val operationType: Operator
    get() = Operator.SUBTRACTION

  override fun checkAnswer(response: Int): Boolean {
    return (response == operand1 - operand2)
  }
}
