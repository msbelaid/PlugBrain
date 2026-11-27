package app.plugbrain.android.challenges.mixed

import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.Operator

class MixedSumInBracketsTimesTwoDigitChallenge : NumericalChallenge {
  val addend1: Int = (10..99).random()
  val addend2: Int = (10..99).random()
  val factor: Int = (10..99).random()
  override val difficultyLevel: Int = 14

  override fun checkAnswer(response: Int): Boolean {
    return response == (addend1 + addend2) * factor
  }

  override fun string(): String =
    "($addend1 ${Operator.ADDITION.symbol} $addend2) ${Operator.MULTIPLICATION.symbol} $factor"
}
