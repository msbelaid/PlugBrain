package app.plugbrain.android.challenges.square

class SquareChallengeTwentyOneThroughFifty : SquareChallenge() {
  override val operand: Int = (21..50).random()

  override val difficultyLevel: Int = 12
}
