package app.plugbrain.android.challenges.percentage

class PercentageFifteenOfMultipleOfTwentyChallenge : PercentageChallenge {
  override val percent: Int = 15
  override val base: Int = (1..20).random() * 20
  override val difficultyLevel: Int = 10

  override fun checkAnswer(response: Int): Boolean {
    return response == percent * base / 100
  }

  override fun string(): String = "$percent% x $base"
}
