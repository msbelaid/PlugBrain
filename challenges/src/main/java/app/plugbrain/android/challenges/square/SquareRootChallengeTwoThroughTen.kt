package app.plugbrain.android.challenges.square

class SquareRootChallengeTwoThroughTen : SquareRootChallenge() {
  override val answer: Int = (2..10).random()

  override val difficultyLevel: Int = 6
}
