package app.plugbrain.android.challenges.substraction

class SubtractThreeFromTwoDigitsChallenge : SubstractionChallenge {
  override val operand1: Int = (100..999).random()
  override val operand2: Int = (10..99).random()
  override val difficultyLevel: Int = 7
}
