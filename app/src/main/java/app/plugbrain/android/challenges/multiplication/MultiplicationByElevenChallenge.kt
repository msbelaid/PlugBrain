package app.plugbrain.android.challenges.multiplication

class MultiplicationByElevenChallenge : MultiplicationChallenge {
  override val operand1: Int = 11
  override val operand2: Int = (2..9).random()
  override val difficultyLevel: Int = 3
}
