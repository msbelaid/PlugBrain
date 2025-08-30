package app.plugbrain.android.challenges.addition

class AdditionFourDigitsChallenge : AdditionChallenge {
  override val operand1: Int = (1000..10000).random()
  override val operand2: Int = (1000..10000).random()
  override val difficultyLevel: Int = 8
}
