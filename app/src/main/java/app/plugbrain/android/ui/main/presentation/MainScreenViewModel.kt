package app.plugbrain.android.ui.main.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.datastore.getTimestamps
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.repository.InstalledAppsRepository
import app.plugbrain.android.repository.PermissionsRepository
import app.plugbrain.android.repository.model.PermissionsState
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

// TODO Create a separate constants file
private const val ONE_HOUR = 60 * 60_000

class MainScreenViewModel(
  private val installedAppsRepository: InstalledAppsRepository,
  private val permissionsRepository: PermissionsRepository,
  private val dataStore: DataStore<Preferences>,
  private val appsUsageStats: AppsUsageStats,
  private val challengeFactory: ChallengeFactory,
) : ViewModel() {
  private val _mainScreenState: MutableStateFlow<MainScreenState?> = MutableStateFlow(null)
  val mainScreenState: StateFlow<MainScreenState?> = _mainScreenState

  private val _permissionsState = MutableStateFlow<PermissionsState?>(null)
  val permissionsState: StateFlow<PermissionsState?> = _permissionsState

  fun getAppsUsageStats() {
    Timber.e("getAppsUsageStats called")

    viewModelScope.launch {
      combine(
        dataStore.getUserSettings(),
        dataStore.getTimestamps(),
        installedAppsRepository.getInstalledApps(),
      ) { settings, timestamps, installedApps ->
        Timber.e("getAppsUsageStats launched")
        val blockedAppsUsage = appsUsageStats.getTotalAppsUsageDuration(
          startTime = timestamps.lastBlock ?: (System.currentTimeMillis() - ONE_HOUR),
          endTime = System.currentTimeMillis(),
          filterPackages = settings.distractiveApps,
        )
        MainScreenState(
          usageFreeDuration =
          if (timestamps.lastUsage != null) (System.currentTimeMillis() - timestamps.lastUsage).milliseconds else null,
          lastUsageDuration = blockedAppsUsage.minutes,
          blockedApps = installedApps.filter {
            it.packageName in settings.distractiveApps
          }.toSet(),
          blockInterval = settings.blockInterval,
          difficultyLevel = settings.currentDifficultyLevel,
          minDifficulty = challengeFactory.minDifficulty(),
          maxDifficulty = challengeFactory.maxDifficulty(),
        )
      }.collect {
        _mainScreenState.value = it
      }
    }
  }

  fun getPermissions() {
    viewModelScope.launch {
      permissionsRepository.getPermissions().collect { state ->
        _permissionsState.value = state
      }
    }
  }
}
