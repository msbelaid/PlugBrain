package app.plugbrain.android.challenges.doubles

import app.plugbrain.android.challenges.multiplication.MultiplicationChallenge

/**
 * Double of a two-digit number. Example: 2 x 68
 */
class DoubleOfTwoDigitChallenge : MultiplicationChallenge {
  override val operand1: Int = 2
  override val operand2: Int = (10..99).random()
  override val difficultyLevel: Int = 4
}
