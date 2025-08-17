package app.plugbrain.android.challenges.multiplication

class MultiplicationSingleByThreeDigitChallenge : MultiplicationChallenge {
  override val operand1: Int = (3..9).random()
  override val operand2: Int = (101..999).random()
  override val difficultyLevel: Int = 10
}
