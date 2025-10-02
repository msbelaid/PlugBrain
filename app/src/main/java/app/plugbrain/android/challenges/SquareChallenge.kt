package app.plugbrain.android.challenges

class SquareChallenge : NumericalChallenge {
  //TODO("Add the challenge fields")
   val operand1: Int = (2..5).random()
   val operand2: Int = (6..10).random()
   val operand3: Int = (2..9).random()

  override fun checkAnswer(response: Int): Boolean {
    TODO("Check the response here!")
//    return operand1 * operand1 == response
    if(operand1 * operand1 == response)
    {
      return true
    }
    else
    {
      return false
    }
  }

  override val difficultyLevel: Int
    get() = TODO("Put difficulty level here")
  //  override val difficultyLevel: Int = 1, 2, or 3
  override fun string(): String {
    TODO("How the challenge should be displayed")
    return operand1.toString() + "² = ?"
  }
}
