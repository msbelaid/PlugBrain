package app.plugbrain.android.challenges.multiplication

class MultiplicationSingleDigitByNineChallenge : MultiplicationChallenge {
  override val operand1: Int = 9
  override val operand2: Int = (2..9).random()
  override val difficultyLevel: Int = 5
}
