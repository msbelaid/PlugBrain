package app.plugbrain.android.challenges.substraction

class SubtractUnderFiveChallenge : SubstractionChallenge {
  override val operand2: Int = (1..5).random()
  override val operand1: Int = (operand2..5).random()
  override val difficultyLevel: Int = 2
}
