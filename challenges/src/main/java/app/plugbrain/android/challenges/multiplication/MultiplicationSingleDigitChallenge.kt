package app.plugbrain.android.challenges.multiplication

class MultiplicationSingleDigitChallenge : MultiplicationChallenge {
  override val operand1: Int = listOf(3, 4, 6, 7, 8).random()
  override val operand2: Int = listOf(3, 4, 6, 7, 8).random()
  override val difficultyLevel: Int = 6
}
