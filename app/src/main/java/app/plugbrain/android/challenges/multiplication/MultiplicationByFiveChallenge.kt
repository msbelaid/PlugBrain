package app.plugbrain.android.challenges.multiplication

class MultiplicationByFiveChallenge : MultiplicationChallenge {
  override val operand1: Int = 5
  override val operand2: Int = (2..9).random()
  override val difficultyLevel: Int = 5
}
