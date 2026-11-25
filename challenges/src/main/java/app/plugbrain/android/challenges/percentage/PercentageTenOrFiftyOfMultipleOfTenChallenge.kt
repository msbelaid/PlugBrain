package app.plugbrain.android.challenges.percentage

class PercentageTenOrFiftyOfMultipleOfTenChallenge : PercentageChallenge {
  override val percent: Int = listOf(10, 50).random()
  override val base: Int = (1..34).random() * 10
  override val difficultyLevel: Int = 6

  override fun checkAnswer(response: Int): Boolean {
    return response == percent * base / 100
  }

  override fun string(): String = "$percent% x $base"
}
