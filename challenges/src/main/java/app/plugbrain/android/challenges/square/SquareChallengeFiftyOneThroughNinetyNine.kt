package app.plugbrain.android.challenges.square

class SquareChallengeFiftyOneThroughNinetyNine : SquareChallenge() {
  override val operand: Int = operands.random()

  override val difficultyLevel: Int = 13

  companion object {
    internal val operands = (51..99).filterNot { it % 5 == 0 }
  }
}
