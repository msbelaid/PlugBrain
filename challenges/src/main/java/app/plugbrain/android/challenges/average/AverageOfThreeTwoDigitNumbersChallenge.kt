package app.plugbrain.android.challenges.average

/**
 * Average of three 2-digit numbers. Examples:
 * - avg(24, 35, 46) (answer: 35)
 * - avg(21, 35, 70) (answer: 42)
 */
class AverageOfThreeTwoDigitNumbersChallenge : AverageChallenge() {
  override val numbers: List<Int> = generate()

  override val difficultyLevel: Int = 11

  private fun generate(): List<Int> {
    // Pick the average first, then fit the three numbers around it, retrying while
    // the third number falls outside 10..99.
    while (true) {
      val average = (10..99).random()
      val first = (10..99).random()
      val second = (10..99).random()
      val third = 3 * average - first - second
      if (third in 10..99) return listOf(first, second, third)
    }
  }
}
