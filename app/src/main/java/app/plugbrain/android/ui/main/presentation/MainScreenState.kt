package app.plugbrain.android.ui.main.presentation

import app.plugbrain.android.repository.model.InstalledApp
import kotlin.time.Duration

data class MainScreenState(
  val usageFreeDuration: Duration? = null,
  val lastUsageDuration: Duration? = null,
  val blockedApps: Set<InstalledApp> = emptySet(),
  val blockInterval: Int = 15,
  val difficultyLevel: Int = 0,
  val minDifficulty: Int = 0,
  val maxDifficulty: Int = 0,
)
