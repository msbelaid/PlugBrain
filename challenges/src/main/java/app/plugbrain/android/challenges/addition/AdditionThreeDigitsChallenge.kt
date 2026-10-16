package app.plugbrain.android.challenges.addition

class AdditionThreeDigitsChallenge : AdditionChallenge {
  override val operand1: Int = (100..999).random()
  override val operand2: Int = (100..999).random()
  override val difficultyLevel: Int = 6
}
