package app.plugbrain.android.ui.challenges.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.datastore.DEFAULT_BLOCK_INTERVAL
import app.plugbrain.android.datastore.decrementProgressiveDifficulty
import app.plugbrain.android.datastore.getTimestamps
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.incrementProgressiveDifficulty
import app.plugbrain.android.datastore.setLastChallengeTime
import app.plugbrain.android.datastore.updateBlockAppsToggle
import app.plugbrain.android.repository.MathChallengeRepository
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArithChallengeViewModel(
  private val dataStore: DataStore<Preferences>,
  private val mathChallengeRepository: MathChallengeRepository,
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

  suspend fun decreaseDifficulty() {
    val (settings, timestamps) = combine(
      dataStore.getUserSettings(),
      dataStore.getTimestamps(),
    ) { settings, timestamps ->
      settings to timestamps
    }.first()

    val durationSinceLastChallenge = (
      System.currentTimeMillis() - (timestamps.lastChallenge ?: 0)
      ).milliseconds.inWholeMinutes.toInt()
    if (durationSinceLastChallenge >= settings.blockInterval * 2) {
      val nbDecLevels = durationSinceLastChallenge / (settings.blockInterval * 2)
      dataStore.decrementProgressiveDifficulty(nbDecLevels)
      dataStore.setLastChallengeTime(System.currentTimeMillis())
    }
  }

  fun unblockApps() {
    viewModelScope.launch {
      dataStore.updateBlockAppsToggle(false)
      dataStore.setLastChallengeTime(System.currentTimeMillis())
      dataStore.incrementProgressiveDifficulty(mathChallengeRepository.getMaxDifficulty())
    }
  }
}
