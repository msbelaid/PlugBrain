package app.plugbrain.android.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import app.plugbrain.android.R
import app.plugbrain.android.challenges.addition.AdditionUnderFiveChallenge
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.selectapps.AppsSelectionActivity
import app.plugbrain.android.ui.settings.compose.SettingsScreen
import app.plugbrain.android.ui.settings.presentation.SettingsViewModel
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : ComponentActivity() {
  private val settingsViewModel: SettingsViewModel by viewModel()

  override fun onResume() {
    super.onResume()
    settingsViewModel.getPermissions()
    settingsViewModel.getChallengeSettings()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val lockedApps by settingsViewModel.getLockedApps().collectAsState(emptyList())
      val permissions by settingsViewModel.permissionsState.collectAsState(PermissionsState())
      val minimalDifficultySample by settingsViewModel.minimalDifficultySample.collectAsState(
        AdditionUnderFiveChallenge(),
      )
      val challengeSettings by settingsViewModel.challengeSettingsState.collectAsState(
        app.plugbrain.android.datastore.model.UserSettings(),
      )
      var showAccessibilityDialog by remember { mutableStateOf(false) }

      MathlockAppTheme {
        SettingsScreen(
          lockedApps = lockedApps,
          permissionsState = permissions,
          blockInterval = challengeSettings.blockInterval,
          minDifficulty = settingsViewModel.getMinDifficulty(),
          maxDifficulty = settingsViewModel.getMaxDifficulty(),
          selectedMinDifficulty = challengeSettings.selectedMinimalDifficulty,
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
            showAccessibilityDialog = true
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
          onMinDifficultySelected = {
            settingsViewModel.updateMinDifficulty(it)
          },
        )
      }
      if (showAccessibilityDialog) {
        AccessibilityDisclosureDialog(
          onContinue = {
            showAccessibilityDialog = false
            openAccessibilitySettings()
          },
          onCancel = {
            showAccessibilityDialog = false
          }
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

  @Composable
  fun AccessibilityDisclosureDialog(
    onContinue: () -> Unit,
    onCancel: () -> Unit
  ) {
    AlertDialog(
      onDismissRequest = { onCancel() },
      icon = {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = stringResource(R.string.accessibility_disclosure_title)
        )
      },
      title = {
        Text(stringResource(R.string.accessibility_disclosure_title), textAlign = TextAlign.Center)
      },
      text = {
        Text(stringResource(R.string.accessibility_disclosure_body), textAlign = TextAlign.Center)
      },
      confirmButton = {
        Button(onClick = onContinue) { Text(stringResource(R.string.global_continue)) }
      },
      dismissButton = {
        TextButton(onClick = onCancel) { Text(stringResource(R.string.global_cancel)) }
      }
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
