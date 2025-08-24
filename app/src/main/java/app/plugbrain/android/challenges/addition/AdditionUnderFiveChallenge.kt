package app.plugbrain.android.challenges.addition

class AdditionUnderFiveChallenge : AdditionChallenge {
  override val operand1: Int = (2..5).random()
  override val operand2: Int = (2..5).random()
  override val difficultyLevel: Int = 1
}
