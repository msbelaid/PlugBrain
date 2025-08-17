package app.plugbrain.android.challenges.addition

class AdditionTwoDigitsWithCarryChallenge : AdditionChallenge {
  override val operand1: Int
  override val operand2: Int
  override val difficultyLevel: Int = 5

  constructor() {
    val units1 = (1..9).random()
    val units2 = ((10 - units1)..9).random()
    val tens1 = (1..9).random()
    val tens2 = (1..9).random()
    this.operand1 = tens1 * 10 + units1
    this.operand2 = tens2 * 10 + units2
  }
}
