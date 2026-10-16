package app.plugbrain.android.challenges.square

class SquareChallengeMediumSixThroughTen : SquareChallenge() {
  override val operand: Int = (6..10).random()

  override val difficultyLevel: Int = 2
}
