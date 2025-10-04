package app.plugbrain.android.challenges

//Abstract base class for "Square" challenges.
abstract class SquareChallenge : NumericalChallenge {
  //TODO("Add the challenge fields")
   abstract val operand: Int

  override fun checkAnswer(response: Int): Boolean {
    TODO("Check the response here!")
    return operand * operand == response
  }

  override val difficultyLevel: Int

  override fun string(): String {
    TODO("How the challenge should be displayed")
    return operand.toString() + "² = ?"
  }
}
