package app.plugbrain.android.challenges.square

class SquareChallengeHardElevenThroughNineteen : SquareChallenge() {
  override val operand: Int = operands.random()

  override val difficultyLevel: Int = 10

  companion object {
    internal val operands = 11..19
  }
}
