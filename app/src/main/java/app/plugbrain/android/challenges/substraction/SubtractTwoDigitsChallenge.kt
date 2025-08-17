package app.plugbrain.android.challenges.substraction

class SubtractTwoDigitsChallenge : SubstractionChallenge {
  override val operand2: Int = (10..99).random()
  override val operand1: Int = ((operand2 + 1)..100).random()
  override val difficultyLevel: Int = 6
}
