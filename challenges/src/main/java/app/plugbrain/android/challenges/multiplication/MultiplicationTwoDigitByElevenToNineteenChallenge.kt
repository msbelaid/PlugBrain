package app.plugbrain.android.challenges.multiplication

class MultiplicationTwoDigitByElevenToNineteenChallenge : MultiplicationChallenge {
  override val operand1: Int = (11..19).random()
  override val operand2: Int = (10..99).random()
  override val difficultyLevel: Int = 11
}
