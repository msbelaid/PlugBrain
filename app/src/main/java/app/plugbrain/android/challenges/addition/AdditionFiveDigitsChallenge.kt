package app.plugbrain.android.challenges.addition

class AdditionFiveDigitsChallenge : AdditionChallenge {
  override val operand1: Int = (10000..99999).random()
  override val operand2: Int = (10000..99999).random()
  override val difficultyLevel: Int = 10
}
