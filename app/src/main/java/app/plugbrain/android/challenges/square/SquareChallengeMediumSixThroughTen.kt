package app.plugbrain.android.challenges.square

import app.plugbrain.android.challenges.square.SquareChallenge

class SquareChallengeMediumSixThroughTen : SquareChallenge() {
  override val operand: Int = (6..10).random()

  override fun checkAnswer(response: Int): Boolean {
    return operand * operand == response
  }

  override val difficultyLevel: Int = 2

  override fun string(): String {
    return operand.toString() + "² = ?"
  }
}
