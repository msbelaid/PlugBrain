package app.plugbrain.android.ui.challenges.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.datastore.DataStoreManager
import app.plugbrain.android.repository.MathChallengeRepository
import app.plugbrain.android.repository.model.MathChallenge
import app.plugbrain.android.service.TAG
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class ArithChallengeViewModel(
  private val dataStoreManager: DataStoreManager,
  private val mathChallengeRepository: MathChallengeRepository,
) : ViewModel() {
  private val _mathChallenge: MutableStateFlow<MathChallenge?> = MutableStateFlow(null)
  val mathChallenge: StateFlow<MathChallenge?> = _mathChallenge

  fun generateChallenge() {
    viewModelScope.launch {
      _mathChallenge.value = mathChallengeRepository.generateProgressiveChallenge().first()
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
