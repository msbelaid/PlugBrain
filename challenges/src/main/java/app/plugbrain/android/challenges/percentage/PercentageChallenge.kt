package app.plugbrain.android.challenges.percentage

import app.plugbrain.android.challenges.NumericalChallenge

// A percentage of a base number, shown as "<percent>% x <base>". Every base is a multiple
// that makes percent * base / 100 a whole number, so the answer is always whole.
interface PercentageChallenge : NumericalChallenge {
  val percent: Int
  val base: Int
}
