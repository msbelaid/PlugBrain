package app.plugbrain.android.challenges.substraction

class SubtractTwoFromOneDigitChallenge : SubstractionChallenge {
  override val operand1: Int = (10..99).random()
  override val operand2: Int = (1..9).random()
  override val difficultyLevel: Int = 4
}
