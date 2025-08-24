package app.plugbrain.android.ui.challenges.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.datastore.DataStoreManager
import app.plugbrain.android.repository.MathChallengeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ArithChallengeViewModel(
  private val dataStoreManager: DataStoreManager,
  private val mathChallengeRepository: MathChallengeRepository,
) : ViewModel() {
  private val _challenge: MutableStateFlow<Challenge?> = MutableStateFlow(null)
  val challenge: StateFlow<Challenge?> = _challenge

  fun generateChallenge() {
    viewModelScope.launch {
      _challenge.value = mathChallengeRepository.generateChallenge().first()
      dataStoreManager.updateLastChallengeTime(System.currentTimeMillis())
    }
  }

  fun unblockApps() {
    viewModelScope.launch {
      dataStoreManager.updateBlockAppsToggle(false)
      dataStoreManager.incrementProgressiveDifficulty()
    }
  }
}
