package app.plugbrain.android.challenges

interface NumericalChallenge : Challenge {
  fun checkAnswer(response: Int): Boolean
}
