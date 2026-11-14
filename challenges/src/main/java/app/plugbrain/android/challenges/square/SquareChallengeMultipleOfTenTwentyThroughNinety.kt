package app.plugbrain.android.challenges.square

class SquareChallengeMultipleOfTenTwentyThroughNinety : SquareChallenge() {
  override val operand: Int = (2..9).random() * 10

  override val difficultyLevel: Int = 5
}
