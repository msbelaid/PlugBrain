package app.plugbrain.android.challenges.square

class SquareChallengeFiftyOneThroughNinetyNine : SquareChallenge() {
  override val operand: Int = (51..99).random()

  override val difficultyLevel: Int = 13
}
