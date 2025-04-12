package app.matholck.android.ui.settings.presentation

import android.app.AppOpsManager
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.Secure
import android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.matholck.android.model.InstalledApp
import app.matholck.android.repository.InstalledAppsRepository
import app.matholck.android.service.MathLockService
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SettingsViewModel(
  private val installedAppsRepository: InstalledAppsRepository,
  private val dataStoreManager: DataStoreManager,
  private val context: Context,
) : ViewModel() {

  private val blockedApps = dataStoreManager.getBlockedApps

  private val _permissionsState = MutableStateFlow<PermissionsState>(PermissionsState())
  val permissionsState: StateFlow<PermissionsState> = _permissionsState

  private val _blockIntervalState = MutableStateFlow<Int>(0)
  val blockIntervalState: StateFlow<Int> = _blockIntervalState

  private fun getInstalledApps(): Flow<List<InstalledApp>> =
    installedAppsRepository.getInstalledApps()

  fun getLockedApps() = combine(blockedApps, getInstalledApps()) { lockedPackages, installed ->
    installed.filter { it.packageName in lockedPackages }
  }

  private fun checkAccessibilityServiceEnabled(): Boolean = Secure
    .getString(context.contentResolver, ENABLED_ACCESSIBILITY_SERVICES)
    ?.contains("${context.packageName}/${MathLockService::class.qualifiedName}")
    ?: false

  private fun isUsageStatsPermissionGranted(): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
      AppOpsManager.OPSTR_GET_USAGE_STATS,
      android.os.Process.myUid(),
      context.packageName,
    )
    return mode == AppOpsManager.MODE_ALLOWED
  }

  private fun isBatteryOptimizationExempted(): Boolean {
    val pm = context.getSystemService(POWER_SERVICE) as PowerManager
    return pm.isIgnoringBatteryOptimizations(context.packageName)
  }

  fun fetchPermission() {
    viewModelScope.launch {
      getPermissions().collect { state ->
        _permissionsState.value = state
      }
    }
  }

  private fun getPermissions(): Flow<PermissionsState> = flow {
    val state = PermissionsState(
      checkAccessibilityServiceEnabled(),
      isUsageStatsPermissionGranted(),
      isBatteryOptimizationExempted(),
      isSystemAlertWindow(),
    )
    emit(state)
  }.flowOn(Dispatchers.IO)

  private fun isSystemAlertWindow(): Boolean = Settings.canDrawOverlays(context)

  fun updateBlockInterval(interval: Int) {
    viewModelScope.launch {
      dataStoreManager.updateBlockInterval(interval)
    }
  }


  fun getBlockInterval() {
    viewModelScope.launch {
      dataStoreManager.getBlockInterval.collect { state ->
        _blockIntervalState.value = state
      }
    }
  }

}
