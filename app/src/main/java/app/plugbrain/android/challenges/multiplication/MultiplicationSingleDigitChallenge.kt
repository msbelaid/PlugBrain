package app.plugbrain.android.challenges.multiplication

class MultiplicationSingleDigitChallenge : MultiplicationChallenge {
  override val operand1: Int = (3..9).minus(5).random()
  override val operand2: Int = (3..9).minus(5).random()
  override val difficultyLevel: Int = 6
}
