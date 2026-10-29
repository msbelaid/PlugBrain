package app.plugbrain.android.ui.onboarding.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.model.UserSettings
import app.plugbrain.android.datastore.saveSetupDefaults
import app.plugbrain.android.datastore.setBlockInterval
import app.plugbrain.android.datastore.setSelectedMinimalDifficulty
import app.plugbrain.android.repository.PermissionsRepository
import app.plugbrain.android.repository.model.PermissionsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val STEP_KEY = "onboarding_step"

class OnboardingViewModel(
  private val permissionsRepository: PermissionsRepository,
  private val dataStore: DataStore<Preferences>,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

  // Kept in the saved state so rotation and process death return to the same step.
  val step: StateFlow<OnboardingStep> = savedStateHandle.getStateFlow(STEP_KEY, OnboardingStep.Welcome)

  private val _permissions = MutableStateFlow(PermissionsState())
  val permissions: StateFlow<PermissionsState> = _permissions.asStateFlow()

  // Choices are saved as soon as they are made, so leaving mid-flow keeps them.
  val settings: StateFlow<UserSettings> = dataStore.getUserSettings()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

  private val _isFinished = MutableStateFlow(false)
  val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

  fun refreshPermissions() {
    viewModelScope.launch {
      permissionsRepository.getPermissions().collect { _permissions.value = it }
    }
  }

  fun next() {
    val next = step.value.next()
    if (next == null) completeSetup() else savedStateHandle[STEP_KEY] = next
  }

  /** Goes to the previous step; false on the first step, where Back leaves the app. */
  fun back(): Boolean {
    val previous = step.value.previous() ?: return false
    savedStateHandle[STEP_KEY] = previous
    return true
  }

  fun updateBlockInterval(minutes: Int) {
    viewModelScope.launch { dataStore.setBlockInterval(minutes) }
  }

  fun updateDifficulty(preset: DifficultyPreset) {
    viewModelScope.launch { dataStore.setSelectedMinimalDifficulty(preset.minimalDifficulty) }
  }

  /**
   * Saves the defaults shown as selected for anything not chosen yet, then finishes. Choices are
   * already saved when made, so this reads DataStore rather than [settings], which can lag behind.
   */
  fun completeSetup() {
    viewModelScope.launch {
      dataStore.saveSetupDefaults()
      _isFinished.value = true
    }
  }

  /** Skipping saves nothing, so whatever wasn't chosen keeps its default. */
  fun finish() {
    _isFinished.value = true
  }
}
