package app.plugbrain.android.challenges

class SquareChallenge : NumericalChallenge {
  //TODO("Add the challenge fields")

  override fun checkAnswer(response: Int): Boolean {
    TODO("Check the response here!")
  }

  override val difficultyLevel: Int
    get() = TODO("Put difficulty level here")

  override fun string(): String {
    TODO("How the challenge should be displayed")
    return operand1.toString() + "² = ?"
  }
}
