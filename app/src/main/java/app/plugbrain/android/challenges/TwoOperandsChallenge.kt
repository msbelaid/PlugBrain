package app.plugbrain.android.challenges

import app.plugbrain.android.repository.model.Operator

interface TwoOperandsChallenge : NumberChallenge {
  val operand1 : Int
  val operand2 : Int
  val operationType: Operator
  override fun string(): String = "$operand1 ${operationType.symbol} $operand2"
}
