package app.plugbrain.android.challenges.average

/**
 * Average of three 2-digit numbers. Examples:
 * - (24 + 35 + 46) ÷ 3 (answer: 35)
 * - (21 + 35 + 70) ÷ 3 (answer: 42)
 */
class AverageOfThreeTwoDigitNumbersChallenge : AverageChallenge() {
  override val numbers: List<Int> = generate()

  override val difficultyLevel: Int = 11

  private fun generate(): List<Int> {
    // Pick the average first, then fit the three numbers around it, retrying while
    // the third number falls outside 10..99.
      val first = (10..99).random()
      val second = (10..99).random()
      val average = ((first+second+10)/3..(first+second+99)/3).random()
      val third = 3 * average - first - second
    }
  }
}
