package app.plugbrain.android.challenges.square

class SquareChallengeEasyTwoThroughFive : SquareChallenge() {
  override val operand: Int = (2..5).random()

  override val difficultyLevel: Int = 1
}
