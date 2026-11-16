package app.plugbrain.android.challenges.average

import app.plugbrain.android.challenges.NumericalChallenge

/**
 * Abstract base class for "average of N numbers" challenges.
 * The average is always a whole number, so the answer is a single number.
 *
 * Example: avg(46, 72) (answer: 59)
 */
abstract class AverageChallenge : NumericalChallenge {
  /** The numbers to average, in the order they are shown. */
  abstract val numbers: List<Int>

  override fun checkAnswer(response: Int): Boolean {
    return response == numbers.sum() / numbers.size
  }

  override fun string(): String {
    return "avg(" + numbers.joinToString(", ") + ")"
  }
}
