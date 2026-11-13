package app.plugbrain.android.challenges.square

class SquareChallengeEndingInFiveFifteenThroughNinetyFive : SquareChallenge() {
  override val operand: Int = (1..9).random() * 10 + 5

  override val difficultyLevel: Int = 9
}
