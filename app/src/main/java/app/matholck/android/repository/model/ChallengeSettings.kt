package app.matholck.android.repository.model

data class ChallengeSettings(
  val operator: Operator = Operator.ADDITION,
  val difficulty: Difficulty = Difficulty.EASY,
  val operationsNumber: Int = 1,
)

enum class Operator(val value: String) {
  ADDITION("+"),
  SUBTRACTION("-"),
  MULTIPLICATION("x"),
}

enum class Difficulty {
  EASY,
  MEDIUM,
  HARD,
}
