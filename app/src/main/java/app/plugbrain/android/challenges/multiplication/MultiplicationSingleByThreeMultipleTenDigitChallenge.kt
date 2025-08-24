package app.plugbrain.android.challenges.multiplication

class MultiplicationSingleByThreeMultipleTenDigitChallenge : MultiplicationChallenge {
  override val operand1: Int = (3..9).random()
  override val operand2: Int = (11..99).random() * 10
  override val difficultyLevel: Int = 9
}
