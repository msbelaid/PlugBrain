package app.plugbrain.android.challenges.mixed

import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.Operator

class MixedProductMinusSingleDigitChallenge : NumericalChallenge {
  val factor1: Int = (2..9).random()
  val factor2: Int = (2..9).random()
  val subtrahend: Int = (1..minOf(9, factor1 * factor2)).random()
  override val difficultyLevel: Int = 7

  override fun checkAnswer(response: Int): Boolean {
    return response == factor1 * factor2 - subtrahend
  }

  override fun string(): String =
    "$factor1 ${Operator.MULTIPLICATION.symbol} $factor2 ${Operator.SUBTRACTION.symbol} $subtrahend"
}
