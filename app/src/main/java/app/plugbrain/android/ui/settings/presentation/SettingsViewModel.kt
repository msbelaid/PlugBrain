package app.plugbrain.android.ui.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class SettingsViewModel(
  private val installedAppsRepository: InstalledAppsRepository,
  private val permissionsRepository: PermissionsRepository,
  private val dataStoreManager: DataStoreManager,
) : ViewModel() {

  private val blockedApps = dataStoreManager.getBlockedApps()

  private val _permissionsState = MutableStateFlow<PermissionsState>(PermissionsState())
  val permissionsState: StateFlow<PermissionsState> = _permissionsState

  private val _blockIntervalState = MutableStateFlow<Int>(0)
  val blockIntervalState: StateFlow<Int> = _blockIntervalState

  private val _challengeSettingsState = MutableStateFlow<ChallengeSettings>(ChallengeSettings())
  val challengeSettingsState: StateFlow<ChallengeSettings> = _challengeSettingsState

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
