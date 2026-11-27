package app.plugbrain.android.challenges.threenumbers

import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.Operator

/**
 * Base class for challenges with three numbers joined by two operators, worked out left to right.
 * Each subclass must keep the running total at zero or more, so the answer is a whole,
 * non-negative number.
 *
 * Example: 7 + 5 - 3 (answer: 9)
 */
abstract class ThreeNumbersChallenge : NumericalChallenge {
  /** The three numbers, in the order they are shown. */
  abstract val numbers: List<Int>

  /** The two operators between the numbers, each ADDITION or SUBTRACTION. */
  abstract val operators: List<Operator>

  override fun checkAnswer(response: Int): Boolean {
    val total = operators.zip(numbers.drop(1)).fold(numbers.first()) { total, (operator, number) ->
      if (operator == Operator.ADDITION) total + number else total - number
    }
    return response == total
  }

  override fun string(): String {
    val rest = operators.zip(numbers.drop(1)).joinToString("") { (operator, number) ->
      " ${operator.symbol} $number"
    }
    return numbers.first().toString() + rest
  }
}
