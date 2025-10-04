package app.plugbrain.android.challenges.square

import app.plugbrain.android.challenges.SquareChallenge

class SquareChallengeEasyTwoThroughFive : SquareChallenge() {
  //TODO("Add the challenge fields")
  override val operand: Int = (2..5).random()

  override fun checkAnswer(response: Int): Boolean {
    TODO("Check the response here!")
    return operand * operand == response
  }

  override val difficultyLevel: Int = 1
  //  override val difficultyLevel: Int = 1, 2, or 3
  override fun string(): String {
    TODO("How the challenge should be displayed")
    return operand.toString() + "² = ?"
  }
}
