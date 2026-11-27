package app.plugbrain.android.challenges.threenumbers

import app.plugbrain.android.challenges.Operator

/**
 * Three 2-digit numbers, plus then minus. Example: 45 + 38 - 27
 */
class ThreeTwoDigitsPlusMinusChallenge : ThreeNumbersChallenge() {
  override val numbers: List<Int> = generate()
  override val operators: List<Operator> = listOf(Operator.ADDITION, Operator.SUBTRACTION)
  override val difficultyLevel: Int = 7

  private fun generate(): List<Int> {
    val first = (10..99).random()
    val second = (10..99).random()
    // The last number is at most the running total, so the total never goes below zero.
    val third = (10..minOf(99, first + second)).random()
    return listOf(first, second, third)
  }
}
