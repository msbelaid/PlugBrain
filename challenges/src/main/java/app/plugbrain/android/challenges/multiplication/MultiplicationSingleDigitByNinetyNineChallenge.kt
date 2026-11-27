package app.plugbrain.android.challenges.multiplication

class MultiplicationSingleDigitByNinetyNineChallenge : MultiplicationChallenge {
  override val operand1: Int = (2..9).random()
  override val operand2: Int = 99
  override val difficultyLevel: Int = 8
}
