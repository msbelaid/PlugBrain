package app.plugbrain.android.challenges.multiplication

class MultiplicationSingleDigitByNumberEndingInNineChallenge : MultiplicationChallenge {
  override val operand1: Int = (2..9).random()
  override val operand2: Int = (1..9).random() * 10 + 9
  override val difficultyLevel: Int = 8
}
