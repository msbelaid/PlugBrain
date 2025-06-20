package app.matholck.android.ui.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.matholck.android.appsusage.AppsUsageStats
import app.matholck.android.datastore.DataStoreManager
import app.matholck.android.repository.InstalledAppsRepository
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
  private val dataStoreManager: DataStoreManager,
  private val appsUsageStats: AppsUsageStats,
) : ViewModel() {
  private val _mainScreenState: MutableStateFlow<MainScreenState?> = MutableStateFlow(null)
  val mainScreenState: StateFlow<MainScreenState?> = _mainScreenState

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
}
