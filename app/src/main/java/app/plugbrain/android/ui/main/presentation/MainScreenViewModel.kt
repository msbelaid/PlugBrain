package app.plugbrain.android.ui.main.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.appsusage.InstalledAppsRepository
import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.datastore.getBlockAppsToggle
import app.plugbrain.android.datastore.getTimestamps
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.hasSavedSetup
import app.plugbrain.android.datastore.setBlockInterval
import app.plugbrain.android.datastore.setChallengeOnAppOpen
import app.plugbrain.android.datastore.setSelectedMinimalDifficulty
import app.plugbrain.android.repository.PermissionsRepository
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.service.usageCountedSince
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

// TODO Create a separate constants file
private const val ONE_MINUTE = 60_000

class MainScreenViewModel(
  private val installedAppsRepository: InstalledAppsRepository,
  private val permissionsRepository: PermissionsRepository,
  private val dataStore: DataStore<Preferences>,
  private val appsUsageStats: AppsUsageStats,
  private val challengeFactory: ChallengeFactory,
) : ViewModel() {
  private val _mainScreenState: MutableStateFlow<MainScreenState?> = MutableStateFlow(null)
  val mainScreenState: StateFlow<MainScreenState?> = _mainScreenState

  private var refreshJob: Job? = null

  /**
   * True until the user finishes onboarding. Users of a version without onboarding, recognised by
   * their blocked apps or granted permissions, are marked as done so they don't go through it again.
   */
  suspend fun needsOnboarding(): Boolean = shouldShowOnboarding(
    permissions = permissionsRepository.getPermissions().first(),
    hasSavedSetup = dataStore.hasSavedSetup().first(),
  )

  /** Reads permissions, settings and usage again, for example when the user comes back to the app. */
  fun refresh() {
    Timber.d("refresh called")
    refreshJob?.cancel()
    refreshJob = viewModelScope.launch {
      combine(
        permissionsRepository.getPermissions(),
        dataStore.getUserSettings(),
        dataStore.getTimestamps(),
        dataStore.getBlockAppsToggle(),
        installedAppsRepository.getInstalledApps(),
      ) { permissions, settings, timestamps, isBlocked, installedApps ->
        val now = System.currentTimeMillis()
        val usedMinutes = appsUsageStats.getTotalAppsUsageDuration(
          startTime = usageCountedSince(timestamps.lastBlock, timestamps.lastUnblock, now),
          endTime = now,
          filterPackages = settings.distractiveApps,
        )
        MainScreenState(
          status = dashboardStatus(
            permissions = permissions,
            hasBlockedApps = settings.distractiveApps.isNotEmpty(),
            isBlocked = isBlocked == true,
            usedMinutes = usedMinutes,
            awayMinutes = timestamps.lastUsage?.takeIf { it > 0 }?.let { (now - it) / ONE_MINUTE },
            blockInterval = settings.blockInterval,
            challengeLevel = settings.currentDifficultyLevel,
            maxChallengeLevel = challengeFactory.maxDifficulty(),
          ),
          blockedApps = installedApps.filter { it.packageName in settings.distractiveApps },
          blockInterval = settings.blockInterval,
          challengeOnAppOpen = settings.challengeOnAppOpen,
          minDifficulty = settings.selectedMinimalDifficulty,
          isBatteryOptimized = !permissions.batteryOptimizationExemption,
        )
      }.collect {
        _mainScreenState.value = it
      }
    }
  }

  /** Saves when to challenge the user; the dashboard picks it up from the settings flow. */
  fun updateChallengeTiming(interval: Int, challengeOnAppOpen: Boolean) {
    viewModelScope.launch {
      dataStore.setBlockInterval(interval)
      dataStore.setChallengeOnAppOpen(challengeOnAppOpen)
    }
  }

  /** A random challenge of [level], to show what that level looks like; null if there is none. */
  fun sampleChallenge(level: Int): String? = challengeFactory.getChallengesByDifficulty(level).randomOrNull()?.string()

  /** Saves the level challenges start at; the dashboard picks it up from the settings flow. */
  fun updateMinDifficulty(level: Int) {
    viewModelScope.launch {
      dataStore.setSelectedMinimalDifficulty(level)
    }
  }
}

/**
 * Onboarding shows while a required permission is missing and nothing of the setup was saved yet,
 * so users who already picked apps, an interval or a difficulty go straight to the main screen.
 */
internal fun shouldShowOnboarding(permissions: PermissionsState, hasSavedSetup: Boolean): Boolean =
  !permissions.areAllImportantPermissionsGranted() && !hasSavedSetup
