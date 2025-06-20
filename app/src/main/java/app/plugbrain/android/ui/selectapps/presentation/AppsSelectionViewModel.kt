package app.plugbrain.android.ui.selectapps.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plugbrain.android.datastore.DataStoreManager
import app.plugbrain.android.repository.InstalledAppsRepository
import app.plugbrain.android.repository.model.InstalledApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppsSelectionViewModel(
  private val installedAppsRepository: InstalledAppsRepository,
  private val dataStoreManager: DataStoreManager,
) : ViewModel() {

  val blockedApps = dataStoreManager.getBlockedApps()

  fun getInstalledApps(): Flow<List<InstalledApp>> =
    installedAppsRepository.getInstalledApps()

  fun blockApp(packageName: String) {
    viewModelScope.launch {
      dataStoreManager.blockApp(packageName)
    }
  }

  fun unblockApp(packageName: String) {
    viewModelScope.launch {
      dataStoreManager.unblockApp(packageName)
    }
  }
}
