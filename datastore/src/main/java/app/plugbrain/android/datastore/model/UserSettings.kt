package app.plugbrain.android.datastore.model

import app.plugbrain.android.datastore.DEFAULT_BLOCK_INTERVAL
import app.plugbrain.android.datastore.DEFAULT_MINIMAL_DIFFICULTY

data class UserSettings(
  val blockInterval: Int = DEFAULT_BLOCK_INTERVAL,
  val distractiveApps: Set<String> = emptySet(),
  val selectedMinimalDifficulty: Int = DEFAULT_MINIMAL_DIFFICULTY,
  val currentDifficultyLevel: Int = DEFAULT_MINIMAL_DIFFICULTY,
)
