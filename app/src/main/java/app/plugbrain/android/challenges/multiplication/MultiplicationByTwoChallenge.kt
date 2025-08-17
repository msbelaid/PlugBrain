package app.plugbrain.android.challenges.multiplication

class MultiplicationByTwoChallenge : MultiplicationChallenge {
  override val operand1: Int = 2
  override val operand2: Int = (2..9).random()
  override val difficultyLevel: Int = 4
}
