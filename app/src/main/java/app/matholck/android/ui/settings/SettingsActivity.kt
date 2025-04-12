package app.matholck.android.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.net.toUri
import app.matholck.android.ui.selectapps.AppsSelectionActivity
import app.matholck.android.ui.settings.presentation.PermissionsState
import app.matholck.android.ui.settings.presentation.SettingsViewModel
import app.matholck.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : ComponentActivity() {
  private val settingsViewModel: SettingsViewModel by viewModel()

  override fun onResume() {
    super.onResume()
    settingsViewModel.fetchPermission()
    settingsViewModel.getBlockInterval()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val lockedApps by settingsViewModel.getLockedApps().collectAsState(emptyList())
      val permissions by settingsViewModel.permissionsState.collectAsState(PermissionsState())
      val blockInterval by settingsViewModel.blockIntervalState.collectAsState(30)
      MathlockAppTheme {
        SettingsScreen(
          lockedApps = lockedApps,
          permissionsState = permissions,
          blockInterval = blockInterval,
          onBlockApplicationsClicked = {
            startActivity(
              Intent(
                this@SettingsActivity,
                AppsSelectionActivity::class.java,
              ),
            )
          },
          onAccessibilityClicked = {
            openAccessibilitySettings()
          },
          onUsageStatsClicked = {
            openUsageStatsPermissionSettings()
          },
          batteryOptimizationClicked = {
            openBatteryOptimizationExemptionSettings()
          },
          onSystemAlertWindow = {
            openManageAlertPermission()
          },
          onUpdateBlockInterval = {
            settingsViewModel.updateBlockInterval(it)
          },
        )
      }
    }
  }

  private fun openManageAlertPermission() {
    val intent = Intent(
      Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
      Uri.parse("package:${packageName}")
    )
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
  }

  private fun openAccessibilitySettings() {
    startActivity(
      Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      },
    )
  }

  private fun openUsageStatsPermissionSettings() {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
  }

  private fun openBatteryOptimizationExemptionSettings() {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
    intent.data = "package:$packageName".toUri()
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
  }
}
