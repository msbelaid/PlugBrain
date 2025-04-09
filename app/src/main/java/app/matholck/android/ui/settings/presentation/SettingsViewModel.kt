package app.matholck.android.ui.settings.presentation

import androidx.lifecycle.ViewModel
import app.matholck.android.model.InstalledApp
import app.matholck.android.repository.InstalledAppsRepository
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SettingsViewModel(
  private val installedAppsRepository: InstalledAppsRepository,
  private val dataStoreManager: DataStoreManager,
) : ViewModel() {

  private val blockedApps = dataStoreManager.getBlockedApps

  private fun getInstalledApps(): Flow<List<InstalledApp>> =
    installedAppsRepository.getInstalledApps()

  fun getLockedApps() = combine(blockedApps, getInstalledApps()) { lockedPackages, installed ->
    installed.filter { it.packageName in lockedPackages }
  }
}
