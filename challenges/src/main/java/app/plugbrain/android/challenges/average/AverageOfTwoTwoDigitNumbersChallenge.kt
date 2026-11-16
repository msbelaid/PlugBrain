package app.plugbrain.android.challenges.average

/**
 * Average of two 2-digit numbers. Examples:
 * - (46 + 72) ÷ 2 (answer: 59)
 * - (34 + 46) ÷ 2 (answer: 40)
 */
class AverageOfTwoTwoDigitNumbersChallenge : AverageChallenge() {
  override val numbers: List<Int> = generate()

  override val difficultyLevel: Int = 8

  private fun generate(): List<Int> {
    val first = (10..99).random()
    // Both numbers keep the same parity, so their sum, and the average, stay whole.
    val second = (10..99).random().withSameParityAs(first)
    return listOf(first, second)
  }

  /**
   * Adjusts this 2-digit number by ±1 if needed to match [other]'s parity (even/odd),
   * ensuring the result stays within the 10..99 range.
   */
  private fun Int.withSameParityAs(other: Int): Int =
    when {
      this % 2 == other % 2 -> this
      this < 99 -> this + 1
      else -> this - 1
    }
}
