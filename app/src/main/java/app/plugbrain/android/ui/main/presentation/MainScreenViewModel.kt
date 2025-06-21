package app.plugbrain.android.ui.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.datastore.DataStoreManager
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
  private val dataStoreManager: DataStoreManager,
  private val appsUsageStats: AppsUsageStats,
) : ViewModel() {
  private val _mainScreenState: MutableStateFlow<MainScreenState?> = MutableStateFlow(null)
  val mainScreenState: StateFlow<MainScreenState?> = _mainScreenState

  private val _permissionsState = MutableStateFlow<PermissionsState?>(null)
  val permissionsState: StateFlow<PermissionsState?> = _permissionsState

  fun getAppsUsageStats() {
    Timber.e("getAppsUsageStats called")

    viewModelScope.launch {
      combine(
        dataStoreManager.getBlockedApps(),
        dataStoreManager.getTimeStats(),
        dataStoreManager.getProgressiveDifficulty(),
        installedAppsRepository.getInstalledApps(),
      ) { blockedApps, timeStats, difficultyLevel, installedApps ->
        Timber.e("getAppsUsageStats launched")
        val blockedAppsUsage = appsUsageStats.getTotalAppsUsageDuration(
          startTime = timeStats.lastBlockTime ?: (System.currentTimeMillis() - ONE_HOUR),
          endTime = System.currentTimeMillis(),
          filterPackages = blockedApps,
        )
        MainScreenState(
          usageFreeDuration =
          if (timeStats.lastUsageTime != null) (System.currentTimeMillis() - timeStats.lastUsageTime).milliseconds else null,
          lastUsageDuration = blockedAppsUsage.minutes,
          blockedApps = installedApps.filter {
            it.packageName in blockedApps
          }.toSet(),
          blockInterval = timeStats.blockInterval,
          difficultyLevel = difficultyLevel,
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
