package app.plugbrain.android.challenges.multiplication

class MultiplicationThreeByFourDigitChallenge : MultiplicationChallenge {
  override val operand1: Int = (101..999).random()
  override val operand2: Int = (1001..9999).random()
  override val difficultyLevel: Int = 14
}
