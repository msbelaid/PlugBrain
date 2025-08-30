package app.plugbrain.android.ui.settings

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
import app.plugbrain.android.challenges.addition.AdditionUnderFiveChallenge
import app.plugbrain.android.repository.model.ChallengeSettings
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.selectapps.AppsSelectionActivity
import app.plugbrain.android.ui.settings.compose.SettingsScreen
import app.plugbrain.android.ui.settings.presentation.SettingsViewModel
import app.plugbrain.android.ui.theme.MathlockAppTheme
import app.plugbrain.android.ui.timer.TimerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : ComponentActivity() {
  private val settingsViewModel: SettingsViewModel by viewModel()

  override fun onResume() {
    super.onResume()
    settingsViewModel.getPermissions()
    settingsViewModel.getBlockInterval()
    settingsViewModel.getChallengeSettings()
    settingsViewModel.getMinDifficulty()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val lockedApps by settingsViewModel.getLockedApps().collectAsState(emptyList())
      val permissions by settingsViewModel.permissionsState.collectAsState(PermissionsState())
      val blockInterval by settingsViewModel.blockIntervalState.collectAsState(5)
      val minimalDifficultySample by settingsViewModel.minimalDifficultySample.collectAsState(
        AdditionUnderFiveChallenge(),
      )
      val minimalDifficulty by settingsViewModel.minimalDifficulty.collectAsState(1)
      val challengeSettings by settingsViewModel.challengeSettingsState.collectAsState(
        ChallengeSettings(),
      )
      MathlockAppTheme {
        SettingsScreen(
          lockedApps = lockedApps,
          permissionsState = permissions,
          challengeSettings = challengeSettings,
          blockInterval = blockInterval,
          maxDifficulty = settingsViewModel.getMaxDifficulty(),
          minDifficulty = minimalDifficulty,
          minDifficultySample = minimalDifficultySample,
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
          onOperationSelected = {
            settingsViewModel.updateChallengeSettings(challengeSettings.copy(operator = it))
          },
          onMinDifficultySelected = {
            settingsViewModel.updateMinDifficulty(it)
          },
          onRefreshClicked = {
            startActivity(Intent(this, TimerActivity::class.java))
          },
        )
      }
    }
  }

  private fun openManageAlertPermission() {
    val intent = Intent(
      Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
      Uri.parse("package:$packageName"),
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

  // TODO add this for Me phones
  fun openMiuiPermissions() {
    try {
      val intent = Intent("miui.intent.action.APP_PERM_EDITOR").apply {
        setClassName(
          "com.miui.securitycenter",
          "com.miui.permcenter.permissions.PermissionsEditorActivity",
        )
        putExtra("extra_pkgname", packageName)
      }
      startActivity(intent)
    } catch (e: Exception) {
      val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
      }
      startActivity(intent)
    }
  }
}
