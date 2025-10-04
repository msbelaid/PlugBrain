package app.plugbrain.android.challenges

/**
 * Abstract base class for "Missing Number" challenges.
 * These challenges present an equation with one missing operand (represented by '?')
 * that the user must determine.
 *
 * Example: 21 / ? = 3 (answer: 7)
 */
abstract class MissingChallenge : NumericalChallenge {
  abstract val operand1: Int
  abstract val operand2: Int
  abstract val result: Int
  abstract val operationType: Operator
  abstract val missingPosition: MissingPosition

  override fun checkAnswer(response: Int): Boolean {
    return when (missingPosition) {
      MissingPosition.FIRST_OPERAND -> response == operand1
      MissingPosition.SECOND_OPERAND -> response == operand2
      MissingPosition.RESULT -> response == result
    }
  }

  override fun string(): String {
    return when (missingPosition) {
      MissingPosition.FIRST_OPERAND -> "? ${operationType.symbol} $operand2 = $result"
      MissingPosition.SECOND_OPERAND -> "$operand1 ${operationType.symbol} ? = $result"
      MissingPosition.RESULT -> "$operand1 ${operationType.symbol} $operand2 = ?"
    }
  }
}

enum class MissingPosition {
  FIRST_OPERAND,
  SECOND_OPERAND,
  RESULT
}