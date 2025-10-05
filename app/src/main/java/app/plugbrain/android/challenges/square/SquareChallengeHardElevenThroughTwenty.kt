package app.plugbrain.android.challenges.square
import app.plugbrain.android.challenges.square.SquareChallenge

class SquareChallengeHardElevenThroughTwenty : SquareChallenge() {
  override val operand: Int = (11..20).random()

  override fun checkAnswer(response: Int): Boolean {
    return operand * operand == response
  }

  override val difficultyLevel: Int = 3

  override fun string(): String {
    return operand.toString() + "² = ?"
  }
}
