package app.plugbrain.android.challenges.multiplication

import app.plugbrain.android.challenges.TwoOperandsChallenge
import app.plugbrain.android.repository.model.Operator

interface MultiplicationChallenge : TwoOperandsChallenge {
  override val operationType: Operator
    get() = Operator.MULTIPLICATION

  override fun checkAnswer(response: Int): Boolean {
    return (response == operand1 * operand2)
  }
}
