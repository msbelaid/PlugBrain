package app.plugbrain.android.challenges.addition

import app.plugbrain.android.challenges.Operator
import app.plugbrain.android.challenges.TwoOperandsChallenge

interface AdditionChallenge : TwoOperandsChallenge {
  override val operationType: Operator
    get() = Operator.ADDITION

  override fun checkAnswer(response: Int): Boolean {
    return (response == operand1 + operand2)
  }
}
