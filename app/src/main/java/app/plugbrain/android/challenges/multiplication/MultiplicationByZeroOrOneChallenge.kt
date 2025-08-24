package app.plugbrain.android.challenges.multiplication

class MultiplicationByZeroOrOneChallenge : MultiplicationChallenge {
  override val operand1: Int = (0..1).random()
  override val operand2: Int = (2..9).random()
  override val difficultyLevel: Int = 1
}
