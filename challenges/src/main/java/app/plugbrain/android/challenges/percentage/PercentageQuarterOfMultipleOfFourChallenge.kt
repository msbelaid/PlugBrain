package app.plugbrain.android.challenges.percentage

class PercentageQuarterOfMultipleOfFourChallenge : PercentageChallenge {
  override val percent: Int = 25
  override val base: Int = (2..25).random() * 4
  override val difficultyLevel: Int = 9

  override fun checkAnswer(response: Int): Boolean {
    return response == percent * base / 100
  }

  override fun string(): String = "$percent% x $base"
}
