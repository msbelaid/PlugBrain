package app.plugbrain.android.challenges.threenumbers

import app.plugbrain.android.challenges.Operator

/**
 * Three single digits, plus then minus. Example: 7 + 5 - 3
 */
class ThreeSingleDigitsPlusMinusChallenge : ThreeNumbersChallenge() {
  override val numbers: List<Int> = generate()
  override val operators: List<Operator> = listOf(Operator.ADDITION, Operator.SUBTRACTION)
  override val difficultyLevel: Int = 4

  private fun generate(): List<Int> {
    val first = (1..9).random()
    val second = (1..9).random()
    // The last number is at most the running total, so the total never goes below zero.
    val third = (1..minOf(9, first + second)).random()
    return listOf(first, second, third)
  }
}
