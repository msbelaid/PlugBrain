package app.plugbrain.android.ui.selectapps.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.datastore.addDistractiveApp
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.removeDistractiveApp
import app.plugbrain.android.repository.InstalledAppsRepository
import app.plugbrain.android.repository.model.InstalledApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppsSelectionViewModel(
  installedAppsRepository: InstalledAppsRepository,
  private val dataStore: DataStore<Preferences>,
) : ViewModel() {

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  val blockedAppsFlow = dataStore.getUserSettings()
    .distinctUntilChanged()
    .map { it.distractiveApps }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())


  private val installedAppsFlow: Flow<List<InstalledApp>> =
    installedAppsRepository.getInstalledApps()
      .distinctUntilChanged()

  @OptIn(ExperimentalCoroutinesApi::class)
  val installedApps = combine(searchQuery, installedAppsFlow, ::Pair)
    .map { pair ->
      InstalledAppsState.Success(
        pair.second
          .filter { installedApp ->
            if (pair.first.isBlank()) return@filter true
            installedApp.packageName.contains(pair.first, ignoreCase = true)
              || installedApp.name.contains(pair.first, ignoreCase = true)
          }
      )
    }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      InstalledAppsState.Loading
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

sealed interface InstalledAppsState {
  data object Loading : InstalledAppsState
  data class Success(val apps: List<InstalledApp>) : InstalledAppsState
}
