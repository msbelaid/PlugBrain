package app.plugbrain.android.repository

import android.app.AppOpsManager
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import android.os.Process
import android.provider.Settings
import android.provider.Settings.Secure
import android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.service.MathLockService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PermissionsRepository(private val context: Context)  {

  fun getPermissions(): Flow<PermissionsState> = flow {
    val state = PermissionsState(
      checkAccessibilityServiceEnabled(),
      isUsageStatsPermissionGranted(),
      isBatteryOptimizationExempted(),
      isSystemAlertWindow(),
    )
    emit(state)
  }.flowOn(Dispatchers.IO)

  private fun checkAccessibilityServiceEnabled(): Boolean = Secure
    .getString(context.contentResolver, ENABLED_ACCESSIBILITY_SERVICES)
    ?.contains("${context.packageName}/${MathLockService::class.qualifiedName}") == true

  private fun isUsageStatsPermissionGranted(): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
      AppOpsManager.OPSTR_GET_USAGE_STATS,
      Process.myUid(),
      context.packageName,
    )
    return mode == AppOpsManager.MODE_ALLOWED
  }

  private fun isBatteryOptimizationExempted(): Boolean {
    val pm = context.getSystemService(POWER_SERVICE) as PowerManager
    return pm.isIgnoringBatteryOptimizations(context.packageName)
  }

  private fun isSystemAlertWindow(): Boolean = Settings.canDrawOverlays(context)
}
