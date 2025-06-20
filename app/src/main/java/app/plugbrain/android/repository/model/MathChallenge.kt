package app.plugbrain.android.repository.model

data class MathChallenge(
  val num1: Int,
  val num2: Int,
  val operator: Operator,
) {
  fun checkAnswer(response: Int) =
    response == when (operator) {
      Operator.ADDITION -> num1 + num2
      Operator.SUBTRACTION -> num1 - num2
      Operator.MULTIPLICATION -> num1 * num2
    }
}
