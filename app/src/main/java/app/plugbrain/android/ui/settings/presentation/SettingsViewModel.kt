package app.plugbrain.android.ui.settings.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.challenges.addition.AdditionUnderFiveChallenge
import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.model.UserSettings
import app.plugbrain.android.datastore.setBlockInterval
import app.plugbrain.android.datastore.setSelectedMinimalDifficulty
import app.plugbrain.android.repository.InstalledAppsRepository
import app.plugbrain.android.repository.PermissionsRepository
import app.plugbrain.android.repository.model.InstalledApp
import app.plugbrain.android.repository.model.PermissionsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
  private val installedAppsRepository: InstalledAppsRepository,
  private val permissionsRepository: PermissionsRepository,
  private val dataStore: DataStore<Preferences>,
  private val challengeFactory: ChallengeFactory,
) : ViewModel() {

  private val blockedApps = dataStore.getUserSettings().map { it.distractiveApps }

  private val _permissionsState = MutableStateFlow(PermissionsState())
  val permissionsState: StateFlow<PermissionsState> = _permissionsState

  private val _challengeSettingsState = MutableStateFlow(UserSettings())
  val challengeSettingsState: StateFlow<UserSettings> = _challengeSettingsState

  private val _minimalDifficultySample = MutableStateFlow<Challenge>(AdditionUnderFiveChallenge())
  val minimalDifficultySample: StateFlow<Challenge> = _minimalDifficultySample

  private val _displayAccessibilityDialog = MutableStateFlow(false)
  val displayAccessibilityDialog: StateFlow<Boolean> = _displayAccessibilityDialog

  fun displayAccessibilityDialog() {
    _displayAccessibilityDialog.update { true }
  }

  fun dismissAccessibilityDialog() {
    _displayAccessibilityDialog.update { false }
  }

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
      dataStore.setBlockInterval(interval)
    }
  }

  fun updateMinDifficulty(minDifficulty: Int) {
    viewModelScope.launch {
      dataStore.setSelectedMinimalDifficulty(minDifficulty)
      _minimalDifficultySample.value = challengeFactory.getChallengeByDifficulty(minDifficulty)
    }
  }

  fun getMaxDifficulty() = challengeFactory.maxDifficulty()

  fun getMinDifficulty() = challengeFactory.minDifficulty()

  fun getChallengeSettings() {
    viewModelScope.launch {
      dataStore.getUserSettings().collect { state ->
        _challengeSettingsState.value = state
        _minimalDifficultySample.value = challengeFactory.getChallengeByDifficulty(state.selectedMinimalDifficulty)
      }
    }
  }
}
