package app.plugbrain.android.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.plugbrain.android.datastore.model.UserSettings
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.main.MainActivity
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset
import app.plugbrain.android.ui.onboarding.presentation.OnboardingStep
import app.plugbrain.android.ui.onboarding.presentation.OnboardingViewModel
import app.plugbrain.android.ui.permissions.openAccessibilitySettings
import app.plugbrain.android.ui.permissions.openBackgroundPopupsSettings
import app.plugbrain.android.ui.permissions.openBatteryOptimizationSettings
import app.plugbrain.android.ui.permissions.openOverlaySettings
import app.plugbrain.android.ui.permissions.openUsageAccessSettings
import app.plugbrain.android.ui.selectapps.presentation.AppsSelectionViewModel
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingActivity : ComponentActivity() {
  private val onboardingViewModel: OnboardingViewModel by viewModel()
  private val appsSelectionViewModel: AppsSelectionViewModel by viewModel()

  override fun onResume() {
    super.onResume()
    // The user may come back from a system settings screen with a permission granted.
    onboardingViewModel.refreshPermissions()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val step by onboardingViewModel.step.collectAsStateWithLifecycle()
      val permissions by onboardingViewModel.permissions.collectAsStateWithLifecycle()
      val settings by onboardingViewModel.settings.collectAsStateWithLifecycle()
      val isFinished by onboardingViewModel.isFinished.collectAsStateWithLifecycle()

      LaunchedEffect(isFinished) {
        if (isFinished) {
          // Main becomes the only screen, so Back from it leaves the app instead of reopening onboarding.
          startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
          finish()
        }
      }

      // On the first step Back isn't handled here, so it leaves the app.
      BackHandler(enabled = step != OnboardingStep.Welcome) { onboardingViewModel.back() }

      MathlockAppTheme {
        OnboardingContent(
          step = step,
          permissions = permissions,
          settings = settings,
        )
      }
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  private fun OnboardingContent(
    step: OnboardingStep,
    permissions: PermissionsState,
    settings: UserSettings,
  ) {
    var explainedPermission by rememberSaveable { mutableStateOf<OnboardingPermission?>(null) }

    AnimatedContent(
      targetState = step,
      transitionSpec = {
        // Forward slides in from the end, Back from the start.
        val direction = if (targetState > initialState) 1 else -1
        (slideInHorizontally { width -> direction * width / 4 } + fadeIn()) togetherWith
          (slideOutHorizontally { width -> -direction * width / 4 } + fadeOut())
      },
      label = "onboarding_step",
    ) { currentStep ->
      when (currentStep) {
        OnboardingStep.Welcome -> WelcomeScreen(onGetStarted = onboardingViewModel::next)

        OnboardingStep.Permissions -> PermissionsStep(
          permissions = permissions,
          onPermissionClick = { permission ->
            when (permission) {
              // Battery optimization asks through a system dialog that explains itself.
              OnboardingPermission.BatteryOptimization -> openBatteryOptimizationSettings()
              // Xiaomi's permission page opens on PlugBrain, and the item says which setting to allow.
              OnboardingPermission.BackgroundPopups -> openBackgroundPopupsSettings()
              else -> explainedPermission = permission
            }
          },
          onProceed = onboardingViewModel::next,
        )

        OnboardingStep.Apps -> {
          // Collected only on this step, so the apps are sorted by usage once usage access is granted.
          val installedApps by appsSelectionViewModel.installedApps.collectAsStateWithLifecycle()
          val blockedApps by appsSelectionViewModel.blockedAppsFlow.collectAsStateWithLifecycle()
          val searchQuery by appsSelectionViewModel.searchQuery.collectAsStateWithLifecycle()
          AppsStep(
            installedAppsState = installedApps,
            blockedApps = blockedApps,
            searchQuery = searchQuery,
            onQueryChange = appsSelectionViewModel::onChangeQuery,
            onAppCheckedChange = { packageName, isChecked ->
              if (isChecked) {
                appsSelectionViewModel.blockApp(packageName)
              } else {
                appsSelectionViewModel.unblockApp(packageName)
              }
            },
            onBlockApps = onboardingViewModel::next,
            onSkip = onboardingViewModel::finish,
          )
        }

        OnboardingStep.Preferences -> PreferencesStep(
          blockInterval = settings.blockInterval,
          difficulty = DifficultyPreset.fromMinimalDifficulty(settings.selectedMinimalDifficulty),
          onBlockIntervalSelected = onboardingViewModel::updateBlockInterval,
          onDifficultySelected = onboardingViewModel::updateDifficulty,
          onFinish = onboardingViewModel::completeSetup,
          onSkip = onboardingViewModel::finish,
        )
      }
    }

    explainedPermission?.let { permission ->
      PermissionExplainerSheet(
        permission = permission,
        onUnderstood = {
          explainedPermission = null
          when (permission) {
            OnboardingPermission.UsageAccess -> openUsageAccessSettings()
            OnboardingPermission.Overlay -> openOverlaySettings()
            OnboardingPermission.Accessibility -> openAccessibilitySettings()
            OnboardingPermission.BatteryOptimization -> openBatteryOptimizationSettings()
            OnboardingPermission.BackgroundPopups -> openBackgroundPopupsSettings()
          }
        },
        onDismiss = { explainedPermission = null },
      )
    }
  }
}
