package app.plugbrain.android.challenges.multiplication

class MultiplicationSingleByDoubleDigitChallenge : MultiplicationChallenge {
  override val operand1: Int = (3..9).random()
  override val operand2: Int = (11..99).random()
  override val difficultyLevel: Int = 8
}
