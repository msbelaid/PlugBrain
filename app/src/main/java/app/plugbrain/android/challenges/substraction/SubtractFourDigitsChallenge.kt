package app.plugbrain.android.challenges.substraction

class SubtractFourDigitsChallenge : SubstractionChallenge {
  override val operand2: Int = (1000..9999).random()
  override val operand1: Int = (operand2..9999).random()
  override val difficultyLevel: Int = 9
}
