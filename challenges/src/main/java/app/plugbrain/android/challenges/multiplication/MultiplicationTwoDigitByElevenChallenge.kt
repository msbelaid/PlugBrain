package app.plugbrain.android.challenges.multiplication

class MultiplicationTwoDigitByElevenChallenge : MultiplicationChallenge {
  override val operand1: Int = 11
  override val operand2: Int = (10..99).random()
  override val difficultyLevel: Int = 7
}
