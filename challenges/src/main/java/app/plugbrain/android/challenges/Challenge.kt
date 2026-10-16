package app.plugbrain.android.challenges

interface Challenge {
  val difficultyLevel: Int
  fun string(): String
}
