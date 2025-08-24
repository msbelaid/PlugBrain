package app.plugbrain.android.challenges.substraction

class SubtractFromZeroChallenge : SubstractionChallenge {
  override val operand1: Int = (1..9).random()
  override val operand2: Int = 0
  override val difficultyLevel: Int = 1
}
