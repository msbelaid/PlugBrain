package app.plugbrain.android.ui.challenges.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.blocking.DifficultyDecay
import app.plugbrain.android.blocking.UsageSource
import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.datastore.DEFAULT_BLOCK_INTERVAL
import app.plugbrain.android.datastore.decrementProgressiveDifficulty
import app.plugbrain.android.datastore.getTimestamps
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.incrementProgressiveDifficulty
import app.plugbrain.android.datastore.setLastChallengeTime
import app.plugbrain.android.datastore.setLastUnblockTime
import app.plugbrain.android.datastore.updateBlockAppsToggle
import app.plugbrain.android.repository.MathChallengeRepository
import app.plugbrain.android.util.Clock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArithChallengeViewModel(
  private val dataStore: DataStore<Preferences>,
  private val mathChallengeRepository: MathChallengeRepository,
  private val clock: Clock,
  private val usageSource: UsageSource,
) : ViewModel() {
  private val _challenge: MutableStateFlow<Challenge?> = MutableStateFlow(null)
  val challenge: StateFlow<Challenge?> = _challenge

  // How long, in minutes, a correct answer unlocks the apps for.
  val blockInterval: StateFlow<Int> = dataStore.getUserSettings()
    .map { it.blockInterval }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DEFAULT_BLOCK_INTERVAL)

  // Only the first call generates a challenge, so leaving and coming back to the
  // challenge screen neither re-rolls it nor applies the difficulty decay again.
  fun generateChallenge() {
    if (_challenge.value != null) return
    viewModelScope.launch {
      decreaseDifficulty()
      _challenge.value = mathChallengeRepository.generateChallenge().first()
    }
  }

  /** Swaps the challenge for a new one at the same level, after its answer was pasted (#238). */
  fun replaceChallenge() {
    viewModelScope.launch {
      _challenge.value = mathChallengeRepository.generateChallenge().first()
    }
  }

  suspend fun decreaseDifficulty() {
    val (settings, timestamps) = combine(
      dataStore.getUserSettings(),
      dataStore.getTimestamps(),
    ) { settings, timestamps ->
      settings to timestamps
    }.first()

    val now = clock.now()
    // Time spent in blocked apps is not time away, so it must not lower the level (#314).
    // Usage stats are read from the system, so keep that off the main thread.
    val minutesInBlockedApps = timestamps.lastChallenge?.let { lastChallenge ->
      withContext(Dispatchers.IO) {
        usageSource.minutesUsed(settings.distractiveApps, from = lastChallenge, to = now)
      }
    } ?: 0L
    val levels = DifficultyDecay.levelsToDrop(
      lastChallenge = timestamps.lastChallenge,
      blockIntervalMinutes = settings.blockInterval,
      now = now,
      minutesInBlockedApps = minutesInBlockedApps,
    )
    if (levels > 0) {
      // The floor (the chosen minimal difficulty) is applied inside the DataStore edit.
      dataStore.decrementProgressiveDifficulty(levels)
      dataStore.setLastChallengeTime(now)
    }
  }

  /**
   * Saves the solve, then calls [onUnblocked]. Leave the challenge screen only from there: once it's
   * gone, MathLockService sees the blocked app again and reopens the challenge if it still reads
   * the apps as blocked.
   */
  fun unblockApps(onUnblocked: () -> Unit = {}) {
    viewModelScope.launch {
      val now = clock.now()
      dataStore.updateBlockAppsToggle(false)
      dataStore.setLastChallengeTime(now)
      // Read by MathLockService so an app isn't challenged again on open right after a solve.
      dataStore.setLastUnblockTime(now)
      dataStore.incrementProgressiveDifficulty(mathChallengeRepository.getMaxDifficulty())
      onUnblocked()
    }
  }
}
