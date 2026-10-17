package app.plugbrain.android.ui.selectapps.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.appsusage.InstalledAppsRepository
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.datastore.addDistractiveApp
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.removeDistractiveApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

private const val USAGE_WINDOW_MILLIS = 24 * 60 * 60 * 1000L

class AppsSelectionViewModel(
  installedAppsRepository: InstalledAppsRepository,
  private val appsUsageStats: AppsUsageStats,
  private val dataStore: DataStore<Preferences>,
) : ViewModel() {

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  val blockedAppsFlow = dataStore.getUserSettings()
    .distinctUntilChanged()
    .map { it.distractiveApps }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

  // Usage is read once per screen so the order doesn't move while the user picks apps.
  // Apps installed afterwards are missing from it and count as unused.
  private var usageByPackage: Map<String, Long>? = null

  private val installedAppsFlow: Flow<List<InstalledApp>> =
    installedAppsRepository.getInstalledApps()
      .distinctUntilChanged()
      .map { apps ->
        val usage = usageByPackage ?: usageLast24h(apps).also { usageByPackage = it }
        apps.sortedByUsage(usage)
      }
      .flowOn(Dispatchers.IO)

  // Without usage access no events come back, so every app counts as unused and the list
  // stays alphabetical.
  private fun usageLast24h(apps: List<InstalledApp>): Map<String, Long> {
    val endTime = System.currentTimeMillis()
    return runCatching {
      appsUsageStats.getUsagePerPackage(
        startTime = endTime - USAGE_WINDOW_MILLIS,
        endTime = endTime,
        filterPackages = apps.map { it.packageName }.toSet(),
      )
    }.onFailure { Timber.w(it, "Could not read apps usage") }
      .getOrDefault(emptyMap())
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  val installedApps = combine(searchQuery, installedAppsFlow, ::Pair)
    .map { pair ->
      InstalledAppsState.Success(
        pair.second
          .filter { installedApp ->
            if (pair.first.isBlank()) return@filter true
            installedApp.packageName.contains(pair.first, ignoreCase = true) ||
              installedApp.name.contains(pair.first, ignoreCase = true)
          },
      )
    }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      InstalledAppsState.Loading,
    )

  fun onChangeQuery(query: String) {
    _searchQuery.value = query
  }

  fun blockApp(packageName: String) {
    viewModelScope.launch {
      dataStore.addDistractiveApp(packageName)
    }
  }

  fun unblockApp(packageName: String) {
    viewModelScope.launch {
      dataStore.removeDistractiveApp(packageName)
    }
  }
}

/**
 * Most used apps first; apps with the same usage, unused ones included, stay alphabetical.
 */
internal fun List<InstalledApp>.sortedByUsage(
  usageByPackage: Map<String, Long>,
): List<InstalledApp> = map { it.copy(usageMillis = usageByPackage[it.packageName] ?: 0L) }
  .sortedWith(compareByDescending<InstalledApp> { it.usageMillis }.thenBy { it.name.lowercase() })

sealed interface InstalledAppsState {
  data object Loading : InstalledAppsState
  data class Success(val apps: List<InstalledApp>) : InstalledAppsState
}
