package app.plugbrain.android.datastore.model

/**
 * The challenge level was set to [level] at [timestamp], when a challenge was solved or the
 * level dropped after time without challenges. The level keeps dropping from there until the
 * next change, see [projectDifficulty][app.plugbrain.android.datastore.projectDifficulty].
 */
data class DifficultyChange(
  val timestamp: Long,
  val level: Int,
)
