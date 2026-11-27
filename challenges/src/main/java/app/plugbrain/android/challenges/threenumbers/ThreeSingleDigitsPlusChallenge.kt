package app.plugbrain.android.challenges.threenumbers

import app.plugbrain.android.challenges.Operator

/**
 * Three single digits, all plus. Example: 8 + 6 + 9
 */
class ThreeSingleDigitsPlusChallenge : ThreeNumbersChallenge() {
  override val numbers: List<Int> = List(3) { (1..9).random() }
  override val operators: List<Operator> = listOf(Operator.ADDITION, Operator.ADDITION)
  override val difficultyLevel: Int = 4
}
