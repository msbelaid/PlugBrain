package app.plugbrain.android.challenges.square

import app.plugbrain.android.challenges.square.SquareChallenge

class SquareChallengeEasyTwoThroughFive : SquareChallenge() {
  override val operand: Int = (2..5).random()

  override fun checkAnswer(response: Int): Boolean {
    return operand * operand == response
  }

  override val difficultyLevel: Int = 1
  //  override val difficultyLevel: Int = 1, 2, or 3
  override fun string(): String {
    return operand.toString() + "² = ?"
  }
}
