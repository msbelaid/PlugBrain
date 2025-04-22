package app.matholck.android.ui.challenges.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.matholck.android.repository.MathChallengeRepository
import app.matholck.android.repository.model.MathChallenge
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArithChallengeViewModel(
  private val dataStoreManager: DataStoreManager,
  private val mathChallengeRepository: MathChallengeRepository,
) : ViewModel() {
  private val _mathChallenge: MutableStateFlow<MathChallenge?> = MutableStateFlow(null)
  val mathChallenge: StateFlow<MathChallenge?> = _mathChallenge

  init {
    generateChallenge()
  }

  private fun generateChallenge() {
    viewModelScope.launch {
      mathChallengeRepository.generateChallenge().collect {
        _mathChallenge.value = it
      }
    }
  }

  fun unblockApps() {
    viewModelScope.launch {
      dataStoreManager.updateBlockAppsToggle(false)
    }
  }
}
