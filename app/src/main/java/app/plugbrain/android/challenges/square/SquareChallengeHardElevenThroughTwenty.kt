package app.plugbrain.android.challenges.square
import app.plugbrain.android.challenges.SquareChallenge

class SquareChallengeHardElevenThroughTwenty : SquareChallenge() {
  //TODO("Add the challenge fields")
  override val operand: Int = (11..20).random()

  override fun checkAnswer(response: Int): Boolean {
    TODO("Check the response here!")
    return operand * operand == response
  }

  override val difficultyLevel: Int = 3
  //  override val difficultyLevel: Int = 1, 2, or 3
  override fun string(): String {
    TODO("How the challenge should be displayed")
    return operand.toString() + "² = ?"
  }
}
