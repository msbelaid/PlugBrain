package app.plugbrain.android.challenges.mixed

import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.Operator

class MixedProductPlusSingleDigitChallenge : NumericalChallenge {
  val factor1: Int = (2..9).random()
  val factor2: Int = (2..9).random()
  val addend: Int = (1..9).random()
  override val difficultyLevel: Int = 7

  override fun checkAnswer(response: Int): Boolean {
    return response == factor1 * factor2 + addend
  }

  override fun string(): String =
    "$factor1 ${Operator.MULTIPLICATION.symbol} $factor2 ${Operator.ADDITION.symbol} $addend"
}
