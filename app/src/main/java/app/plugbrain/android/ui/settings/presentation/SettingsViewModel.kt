package app.plugbrain.android.ui.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.challenges.addition.AdditionUnderFiveChallenge
import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.datastore.DataStoreManager
import app.plugbrain.android.repository.InstalledAppsRepository
import app.plugbrain.android.repository.PermissionsRepository
import app.plugbrain.android.repository.model.ChallengeSettings
import app.plugbrain.android.repository.model.InstalledApp
import app.plugbrain.android.repository.model.PermissionsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(
  private val installedAppsRepository: InstalledAppsRepository,
  private val permissionsRepository: PermissionsRepository,
  private val dataStoreManager: DataStoreManager,
  private val challengeFactory: ChallengeFactory,
) : ViewModel() {

  private val blockedApps = dataStoreManager.getBlockedApps()

  private val _permissionsState = MutableStateFlow<PermissionsState>(PermissionsState())
  val permissionsState: StateFlow<PermissionsState> = _permissionsState

  private val _blockIntervalState = MutableStateFlow<Int>(0)
  val blockIntervalState: StateFlow<Int> = _blockIntervalState

  private val _challengeSettingsState = MutableStateFlow<ChallengeSettings>(ChallengeSettings())
  val challengeSettingsState: StateFlow<ChallengeSettings> = _challengeSettingsState

  private val _minimalDifficulty = MutableStateFlow<Int>(1)
  val minimalDifficulty: StateFlow<Int> = _minimalDifficulty

  private val _minimalDifficultySample = MutableStateFlow<Challenge>(AdditionUnderFiveChallenge())
  val minimalDifficultySample: StateFlow<Challenge> = _minimalDifficultySample

  private fun getInstalledApps(): Flow<List<InstalledApp>> =
    installedAppsRepository.getInstalledApps()

  fun getLockedApps() = combine(blockedApps, getInstalledApps()) { lockedPackages, installed ->
    installed.filter { it.packageName in lockedPackages }
  }

  fun getPermissions() {
    viewModelScope.launch {
      permissionsRepository.getPermissions().collect { state ->
        _permissionsState.value = state
      }
    }
  }

  fun updateBlockInterval(interval: Int) {
    viewModelScope.launch {
      dataStoreManager.updateBlockInterval(interval)
    }
  }

  fun getBlockInterval() {
    viewModelScope.launch {
      dataStoreManager.getBlockInterval().collect { state ->
        _blockIntervalState.value = state
      }
    }
  }

  fun updateMinDifficulty(minDifficulty: Int) {
    viewModelScope.launch {
      Timber.d("updateMinDifficulty: $minDifficulty")
      dataStoreManager.updateMinimalDifficulty(minDifficulty)
      _minimalDifficultySample.value = challengeFactory.getChallengeByDifficulty(minDifficulty)
    }
  }

  fun getSelectedMinDifficulty() {
    viewModelScope.launch {
      dataStoreManager.getMinimalDifficulty().collect { state ->
        _minimalDifficulty.value = state
        Timber.d("getMinDifficulty: $state")
        _minimalDifficultySample.value = challengeFactory.getChallengeByDifficulty(state)
      }
    }
  }

  fun getMaxDifficulty() = dataStoreManager.getMaxDifficulty()

  fun getMinDifficulty() = dataStoreManager.getMinDifficulty()

  fun getChallengeSettings() {
    viewModelScope.launch {
      dataStoreManager.getChallengeSettings().collect { state ->
        _challengeSettingsState.value = state
      }
    }
  }

  fun updateChallengeSettings(challengeSettings: ChallengeSettings) {
    viewModelScope.launch {
      dataStoreManager.updateDifficulty(challengeSettings.difficulty)
      dataStoreManager.updateOperator(challengeSettings.operator)
    }
  }
}
