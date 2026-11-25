package app.plugbrain.android.challenges.percentage

class PercentageTwentyOrFiveOfMultipleOfTwentyChallenge : PercentageChallenge {
  override val percent: Int = listOf(20, 5).random()
  override val base: Int = (1..20).random() * 20
  override val difficultyLevel: Int = 8

  override fun checkAnswer(response: Int): Boolean {
    return response == percent * base / 100
  }

  override fun string(): String = "$percent% x $base"
}
