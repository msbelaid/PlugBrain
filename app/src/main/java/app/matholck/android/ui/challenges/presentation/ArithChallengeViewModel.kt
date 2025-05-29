package app.matholck.android.ui.challenges.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.matholck.android.datastore.DataStoreManager
import app.matholck.android.repository.MathChallengeRepository
import app.matholck.android.repository.model.MathChallenge
import app.matholck.android.service.TAG
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

class ArithChallengeViewModel(
  private val dataStoreManager: DataStoreManager,
  private val mathChallengeRepository: MathChallengeRepository,
) : ViewModel() {
  private val _mathChallenge: MutableStateFlow<MathChallenge?> = MutableStateFlow(null)
  val mathChallenge: StateFlow<MathChallenge?> = _mathChallenge

  init {
    decreaseDifficulty()
    generateChallenge()
  }

  private fun generateChallenge() {
    viewModelScope.launch {
      mathChallengeRepository.generateProgressiveChallenge().collect {
        _mathChallenge.value = it
      }
    }
  }

  fun unblockApps() {
    viewModelScope.launch {
      dataStoreManager.updateBlockAppsToggle(false)
      dataStoreManager.incrementProgressiveDifficulty()
    }
  }

  fun decreaseDifficulty() {
    viewModelScope.launch {
      combine(
        dataStoreManager.getLastUsageTime(),
        dataStoreManager.getBlockInterval(),
      ) { lastUsageTime, blockInterval ->
        (lastUsageTime to blockInterval)
      }.collect {
        val durationSinceLastUsage = (
          System.currentTimeMillis() - (
            it.first
              ?: 0
            )
          ).milliseconds.inWholeMinutes.toInt()
        Timber.tag(TAG).e(durationSinceLastUsage.toString())
        if (durationSinceLastUsage >= it.second * 2) {
          dataStoreManager.decrementProgressiveDifficulty(durationSinceLastUsage / (it.second * 2))
        }
      }
    }
  }
}
