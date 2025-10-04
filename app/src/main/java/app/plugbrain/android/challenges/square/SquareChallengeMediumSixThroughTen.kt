package app.plugbrain.android.challenges.square

import app.plugbrain.android.challenges.SquareChallenge

class SquareChallengeMediumSixThroughTen : SquareChallenge() {
  //TODO("Add the challenge fields")
  override val operand: Int = (6..10).random()

  override fun checkAnswer(response: Int): Boolean {
    TODO("Check the response here!")
    return operand * operand == response
  }

  override val difficultyLevel: Int = 2

  override fun string(): String {
    TODO("How the challenge should be displayed")
    return operand.toString() + "² = ?"
  }
}
