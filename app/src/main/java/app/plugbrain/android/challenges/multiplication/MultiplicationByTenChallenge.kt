package app.plugbrain.android.challenges.multiplication

class MultiplicationByTenChallenge : MultiplicationChallenge {
  override val operand1: Int = 10
  override val operand2: Int = (2..10).random()
  override val difficultyLevel: Int = 2
}
