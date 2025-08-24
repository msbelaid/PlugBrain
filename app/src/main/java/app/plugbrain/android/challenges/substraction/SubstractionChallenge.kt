package app.plugbrain.android.challenges.substraction

import app.plugbrain.android.challenges.TwoOperandsChallenge
import app.plugbrain.android.repository.model.Operator

interface SubstractionChallenge : TwoOperandsChallenge {
  override val operationType: Operator
    get() = Operator.SUBTRACTION

  override fun checkAnswer(response: Int): Boolean {
    return (response == operand1 - operand2)
  }
}
