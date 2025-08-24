package app.plugbrain.android.datastore.model

data class Settings(
  val blockInterval: Int,
  val distractiveApps: Set<String>,
  val selectedMinimalDifficulty: Int,
)
