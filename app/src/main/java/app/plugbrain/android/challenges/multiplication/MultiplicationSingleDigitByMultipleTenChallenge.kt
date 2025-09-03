package app.plugbrain.android.challenges.multiplication

class MultiplicationSingleDigitByMultipleTenChallenge : MultiplicationChallenge {
  override val operand1: Int = (3..9).random()
  override val operand2: Int = (2..9).random() * 10
  override val difficultyLevel: Int = 7
}
