package app.plugbrain.android.challenges.substraction

class SubtractThreeDigitsChallenge : SubstractionChallenge {
  override val operand2: Int = (100..999).random()
  override val operand1: Int = (operand2..999).random()
  override val difficultyLevel: Int = 8
}
