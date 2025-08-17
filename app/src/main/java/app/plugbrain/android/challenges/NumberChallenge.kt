package app.plugbrain.android.challenges

interface NumberChallenge : Challenge {
  fun checkAnswer(response: Int): Boolean
}
