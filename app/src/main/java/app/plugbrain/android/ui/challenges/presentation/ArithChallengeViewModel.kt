package app.plugbrain.android.ui.challenges.presentation

import android.util.Range
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

  private val _countChallenge: MutableStateFlow<Int> = MutableStateFlow(1)
  val countChallenge: StateFlow<Int> = _countChallenge

  fun generateCountChallenge() {
    _countChallenge.value = (1..10).random()
  }

  fun generateArithChallenge() {
    viewModelScope.launch {
      decreaseDifficulty()
      _mathChallenge.value = mathChallengeRepository.generateProgressiveChallenge().first()
      dataStoreManager.updateLastChallengeTime(System.currentTimeMillis())
    }
  }

  suspend fun decreaseDifficulty() {
    val it = dataStoreManager.getTimeStats().first()
    val durationSinceLastChallenge = (
      System.currentTimeMillis() - (it.lastChallengeGenerateTime ?: 0)
      ).milliseconds.inWholeMinutes.toInt()
    Timber.tag(TAG).e("Duration since last challenge:%s", durationSinceLastChallenge.toString())
    if (durationSinceLastChallenge >= it.blockInterval * 2) {
      val nbDecLevels = durationSinceLastChallenge / (it.blockInterval * 2)
      dataStoreManager.decrementProgressiveDifficulty(nbDecLevels)
    }
  }

  fun unblockApps() {
    viewModelScope.launch {
      dataStoreManager.updateBlockAppsToggle(false)
      dataStoreManager.incrementProgressiveDifficulty()
    }
  }
}
