package app.plugbrain.android.challenges.substraction

class SubtractOneDigitChallenge : SubstractionChallenge {
  override val operand2: Int = (3..8).random()
  override val operand1: Int = (operand2 + 1..9).random()
  override val difficultyLevel: Int = 3
}
