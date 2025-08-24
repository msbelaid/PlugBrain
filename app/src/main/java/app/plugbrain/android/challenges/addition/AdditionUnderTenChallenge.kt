package app.plugbrain.android.challenges.addition

class AdditionUnderTenChallenge : AdditionChallenge {
  override val operand1: Int = (2..9).random()
  override val operand2: Int = (2..9).random()
  override val difficultyLevel: Int = 2
}
