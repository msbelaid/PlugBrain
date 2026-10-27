package app.plugbrain.android.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import app.plugbrain.android.BuildConfig
import app.plugbrain.android.ui.about.AboutLink
import app.plugbrain.android.ui.about.AboutSheet
import app.plugbrain.android.ui.blockinterval.BlockIntervalSheet
import app.plugbrain.android.ui.main.compose.MainScreen
import app.plugbrain.android.ui.main.presentation.MainScreenViewModel
import app.plugbrain.android.ui.main.presentation.RequiredPermission
import app.plugbrain.android.ui.mindifficulty.MinDifficultySheet
import app.plugbrain.android.ui.onboarding.OnboardingActivity
import app.plugbrain.android.ui.onboarding.OnboardingPermission
import app.plugbrain.android.ui.onboarding.PermissionExplainerSheet
import app.plugbrain.android.ui.permissions.openAccessibilitySettings
import app.plugbrain.android.ui.permissions.openBatteryOptimizationSettings
import app.plugbrain.android.ui.permissions.openOverlaySettings
import app.plugbrain.android.ui.permissions.openUsageAccessSettings
import app.plugbrain.android.ui.selectapps.AppsSelectionActivity
import app.plugbrain.android.ui.theme.MathlockAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
  private val mainScreenViewModel: MainScreenViewModel by viewModel()

  // Set once we know onboarding is done; until then nothing is shown or refreshed.
  private var isReady = false

  override fun onResume() {
    super.onResume()
    if (isReady) mainScreenViewModel.refresh()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    lifecycleScope.launch {
      if (mainScreenViewModel.needsOnboarding()) {
        startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
        finish()
        return@launch
      }
      showMainScreen()
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  private fun showMainScreen() {
    isReady = true
    setContent {
      val state by mainScreenViewModel.mainScreenState.collectAsState()
      var explainedPermission by rememberSaveable { mutableStateOf<RequiredPermission?>(null) }
      var isEditingBlockInterval by rememberSaveable { mutableStateOf(false) }
      var isEditingMinDifficulty by rememberSaveable { mutableStateOf(false) }
      var isShowingAbout by rememberSaveable { mutableStateOf(false) }
      MathlockAppTheme {
        MainScreen(
          state = state,
          onAppsClicked = { navigateTo(AppsSelectionActivity::class.java) },
          onBlockIntervalClicked = { isEditingBlockInterval = true },
          onMinDifficultyClicked = { isEditingMinDifficulty = true },
          // Like onboarding: the system dialog explains itself, so no sheet first.
          onBatteryOptimizationClicked = { openBatteryOptimizationSettings() },
          onTurnBackOn = { explainedPermission = it },
          onAboutClicked = { isShowingAbout = true },
        )

        // The same sheet as onboarding: where to find PlugBrain, then the system settings screen.
        explainedPermission?.let { permission ->
          PermissionExplainerSheet(
            permission = permission.toOnboardingPermission(),
            onUnderstood = {
              explainedPermission = null
              when (permission) {
                RequiredPermission.Accessibility -> openAccessibilitySettings()
                RequiredPermission.UsageAccess -> openUsageAccessSettings()
                RequiredPermission.Overlay -> openOverlaySettings()
              }
            },
            onDismiss = { explainedPermission = null },
          )
        }

        if (isShowingAbout) {
          AboutSheet(
            versionName = BuildConfig.VERSION_NAME,
            onLinkClicked = { openLink(it) },
            onDismiss = { isShowingAbout = false },
          )
        }

        val currentState = state
        if (isEditingBlockInterval && currentState != null) {
          BlockIntervalSheet(
            blockInterval = currentState.blockInterval,
            challengeOnAppOpen = currentState.challengeOnAppOpen,
            onSave = { interval, onOpen ->
              mainScreenViewModel.updateChallengeTiming(interval, onOpen)
              isEditingBlockInterval = false
            },
            onDismiss = { isEditingBlockInterval = false },
          )
        }

        val minDifficulty = state?.minDifficulty
        if (isEditingMinDifficulty && minDifficulty != null) {
          MinDifficultySheet(
            minDifficulty = minDifficulty,
            sampleChallenge = mainScreenViewModel::sampleChallenge,
            onSave = {
              mainScreenViewModel.updateMinDifficulty(it)
              isEditingMinDifficulty = false
            },
            onDismiss = { isEditingMinDifficulty = false },
          )
        }
      }
    }
    mainScreenViewModel.refresh()
  }

  private fun navigateTo(activity: Class<*>) {
    startActivity(Intent(this, activity))
  }

  // Some devices, like kiosk or work profiles, have no browser to open the link in.
  private fun openLink(link: AboutLink) {
    try {
      startActivity(Intent(Intent.ACTION_VIEW, link.url.toUri()))
    } catch (e: ActivityNotFoundException) {
      Timber.w(e, "No app to open %s", link.url)
    }
  }
}

private fun RequiredPermission.toOnboardingPermission() = when (this) {
  RequiredPermission.Accessibility -> OnboardingPermission.Accessibility
  RequiredPermission.UsageAccess -> OnboardingPermission.UsageAccess
  RequiredPermission.Overlay -> OnboardingPermission.Overlay
}
