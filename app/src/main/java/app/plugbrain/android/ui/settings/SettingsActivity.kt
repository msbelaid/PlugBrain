package app.plugbrain.android.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import app.plugbrain.android.R
import app.plugbrain.android.challenges.addition.AdditionUnderFiveChallenge
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.permissions.openAccessibilitySettings
import app.plugbrain.android.ui.permissions.openBackgroundPopupsSettings
import app.plugbrain.android.ui.permissions.openBatteryOptimizationSettings
import app.plugbrain.android.ui.permissions.openOverlaySettings
import app.plugbrain.android.ui.permissions.openUsageAccessSettings
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

  override fun onPause() {
    super.onPause()
    settingsViewModel.dismissAccessibilityDialog()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val lockedApps by settingsViewModel.getLockedApps().collectAsState(emptyList())
      val permissions by settingsViewModel.permissionsState.collectAsState(PermissionsState())
      val displayAccessibilityDialog by settingsViewModel.displayAccessibilityDialog.collectAsState(false)
      val minimalDifficultySample by settingsViewModel.minimalDifficultySample.collectAsState(
        AdditionUnderFiveChallenge(),
      )
      val challengeSettings by settingsViewModel.challengeSettingsState.collectAsState(
        app.plugbrain.android.datastore.model.UserSettings(),
      )
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
            settingsViewModel.displayAccessibilityDialog()
          },
          onUsageStatsClicked = {
            openUsageAccessSettings()
          },
          batteryOptimizationClicked = {
            openBatteryOptimizationSettings()
          },
          onSystemAlertWindow = {
            openOverlaySettings()
          },
          onBackgroundPopupsClicked = {
            openBackgroundPopupsSettings()
          },
          onUpdateBlockInterval = {
            settingsViewModel.updateBlockInterval(it)
          },
          onMinDifficultySelected = {
            settingsViewModel.updateMinDifficulty(it)
          },
        )

        // Disclosure Dialog for Accessibility Service
        if (displayAccessibilityDialog) {
          AlertDialog(
            onDismissRequest = {
              settingsViewModel.dismissAccessibilityDialog()
            },
            title = {
              Text(getString(R.string.enable_accessibility_title))
            },
            text = {
              Text(getString(R.string.enable_accessibility_text))
            },
            icon = {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = getString(R.string.enable_accessibility_title),
              )
            },
            confirmButton = {
              Button(
                onClick = { openAccessibilitySettings() },
              ) {
                Text(stringResource(R.string.continue_btn))
              }
            },
            dismissButton = {
              TextButton(
                onClick = { settingsViewModel.dismissAccessibilityDialog() },
              ) {
                Text(stringResource(R.string.cancel_btn))
              }
            },
          )
        }
      }
    }
  }
}
