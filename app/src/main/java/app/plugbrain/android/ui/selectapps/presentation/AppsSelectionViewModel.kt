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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AppsSelectionViewModel(
  private val installedAppsRepository: InstalledAppsRepository,
  private val dataStore: DataStore<Preferences>,
) : ViewModel() {

  val blockedApps = dataStore.getUserSettings().map { it.distractiveApps }

  fun getInstalledApps(): Flow<List<InstalledApp>> =
    installedAppsRepository.getInstalledApps()

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
