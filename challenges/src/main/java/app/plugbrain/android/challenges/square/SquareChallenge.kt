package app.plugbrain.android.challenges.square

import app.plugbrain.android.challenges.NumericalChallenge

// Abstract base class for "Square" challenges.
abstract class SquareChallenge : NumericalChallenge {
  abstract val operand: Int
  override fun checkAnswer(response: Int): Boolean {
    return operand * operand == response
  }
  abstract override val difficultyLevel: Int
  override fun string(): String {
    return operand.toString() + "² = ?"
  }
}
