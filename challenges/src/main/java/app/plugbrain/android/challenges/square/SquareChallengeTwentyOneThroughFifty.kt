package app.plugbrain.android.challenges.square

class SquareChallengeTwentyOneThroughFifty : SquareChallenge() {
  override val operand: Int = operands.random()

  override val difficultyLevel: Int = 12

  companion object {
    internal val operands = (21..50).filterNot { it % 5 == 0 }
  }
}
