package app.plugbrain.android.challenges.square

import app.plugbrain.android.challenges.NumericalChallenge

/**
 * Abstract base class for "Square root" challenges.
 * The answer is picked first and then squared, so the root is always a whole number.
 *
 * Example: √49 = ? (answer: 7)
 */
abstract class SquareRootChallenge : NumericalChallenge {
  /** The root, which is the number the user enters. */
  abstract val answer: Int

  override fun checkAnswer(response: Int): Boolean {
    return answer == response
  }

  abstract override val difficultyLevel: Int

  override fun string(): String {
    return "√${answer * answer} = ?"
  }
}
