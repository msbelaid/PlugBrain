package app.plugbrain.android.challenges.mixed

import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.Operator

class MixedTwoProductsAddedChallenge : NumericalChallenge {
  val first1: Int = (2..9).random()
  val first2: Int = (2..9).random()
  val second1: Int = (2..9).random()
  val second2: Int = (2..9).random()
  override val difficultyLevel: Int = 12

  override fun checkAnswer(response: Int): Boolean {
    return response == first1 * first2 + second1 * second2
  }

  override fun string(): String =
    "$first1 ${Operator.MULTIPLICATION.symbol} $first2 ${Operator.ADDITION.symbol} " +
      "$second1 ${Operator.MULTIPLICATION.symbol} $second2"
}
