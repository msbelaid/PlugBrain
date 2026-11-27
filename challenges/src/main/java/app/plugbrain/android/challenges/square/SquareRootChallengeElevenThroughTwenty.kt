package app.plugbrain.android.challenges.square

class SquareRootChallengeElevenThroughTwenty : SquareRootChallenge() {
  override val answer: Int = (11..20).random()

  override val difficultyLevel: Int = 11
}
