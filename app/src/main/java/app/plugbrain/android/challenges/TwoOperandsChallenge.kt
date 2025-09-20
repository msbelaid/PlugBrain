package app.plugbrain.android.challenges

interface TwoOperandsChallenge : NumericalChallenge {
  val operand1: Int
  val operand2: Int
  val operationType: Operator
  override fun string(): String = "$operand1 ${operationType.symbol} $operand2"
}

enum class Operator(val symbol: String) {
  ADDITION("+"),
  SUBTRACTION("-"),
  MULTIPLICATION("x"),
}
