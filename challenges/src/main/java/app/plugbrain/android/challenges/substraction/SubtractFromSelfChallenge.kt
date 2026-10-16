package app.plugbrain.android.challenges.substraction

class SubtractFromSelfChallenge : SubstractionChallenge {
  override val operand1: Int = (1..9).random()
  override val operand2: Int = operand1
  override val difficultyLevel: Int = 1
}
