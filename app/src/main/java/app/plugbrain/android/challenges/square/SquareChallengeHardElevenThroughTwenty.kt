package app.plugbrain.android.challenges.square

class SquareChallengeHardElevenThroughTwenty : SquareChallenge() {
  override val operand: Int = (11..20).random()

  override val difficultyLevel: Int = 3
}
