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
    val first = (10..99).random()
    val second = (10..99).random()
    // The range keeps third in 10..99. It adds 12, not 10, because integer division floors.
    val average = ((first + second + 12) / 3..(first + second + 99) / 3).random()
    val third = 3 * average - first - second
    return listOf(first, second, third)
  }
}
