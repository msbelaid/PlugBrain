package app.plugbrain.android.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessibilityNew
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.designsystem.components.layout.PlugStepScaffold
import app.plugbrain.android.ui.designsystem.components.listitem.PlugPermissionListItem
import app.plugbrain.android.ui.onboarding.presentation.OnboardingStep
import app.plugbrain.android.ui.theme.PlugBrainTheme

@Composable
fun PermissionsStep(
  permissions: PermissionsState,
  onPermissionClick: (OnboardingPermission) -> Unit,
  onProceed: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val canProceed = permissions.areAllImportantPermissionsGranted()
  PlugStepScaffold(
    modifier = modifier,
    title = stringResource(R.string.onboarding_permissions_title),
    subtitle = stringResource(R.string.onboarding_permissions_subtitle),
    currentStep = OnboardingStep.Permissions.progress,
    totalSteps = OnboardingStep.progressSteps,
    primaryCtaText = stringResource(R.string.onboarding_permissions_cta),
    onPrimaryCtaClick = onProceed,
    isPrimaryCtaEnabled = canProceed,
    helperText = if (canProceed) null else stringResource(R.string.onboarding_permissions_helper),
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      PermissionItem(
        permission = OnboardingPermission.UsageAccess,
        isGranted = permissions.usageStatsPermission,
        onClick = onPermissionClick,
      )
      PermissionItem(
        permission = OnboardingPermission.Overlay,
        isGranted = permissions.systemAlertWindow,
        onClick = onPermissionClick,
      )
      PermissionItem(
        permission = OnboardingPermission.Accessibility,
        isGranted = permissions.accessibilityPermission,
        onClick = onPermissionClick,
      )
      PermissionItem(
        permission = OnboardingPermission.BatteryOptimization,
        isGranted = permissions.batteryOptimizationExemption,
        onClick = onPermissionClick,
      )
    }
  }
}

@Composable
private fun PermissionItem(
  permission: OnboardingPermission,
  isGranted: Boolean,
  onClick: (OnboardingPermission) -> Unit,
) {
  val (title, description, icon) = when (permission) {
    OnboardingPermission.UsageAccess -> Triple(
      R.string.onboarding_usage_access_title,
      R.string.onboarding_usage_access_description,
      Icons.Rounded.Search,
    )
    OnboardingPermission.Overlay -> Triple(
      R.string.onboarding_overlay_title,
      R.string.onboarding_overlay_description,
      Icons.Rounded.Layers,
    )
    OnboardingPermission.Accessibility -> Triple(
      R.string.onboarding_accessibility_title,
      R.string.onboarding_accessibility_description,
      Icons.Rounded.AccessibilityNew,
    )
    OnboardingPermission.BatteryOptimization -> Triple(
      R.string.onboarding_battery_title,
      R.string.onboarding_battery_description,
      Icons.Rounded.BatteryChargingFull,
    )
  }
  PlugPermissionListItem(
    title = stringResource(title),
    description = stringResource(description),
    icon = icon,
    isGranted = isGranted,
    // A granted permission can only be revoked from the system settings, so there is nothing to do here.
    onClick = { if (!isGranted) onClick(permission) },
  )
}

@Preview(showSystemUi = true)
@Composable
private fun PermissionsStepPreview() {
  PlugBrainTheme(dynamicColor = false) {
    PermissionsStep(
      permissions = PermissionsState(usageStatsPermission = true),
      onPermissionClick = {},
      onProceed = {},
    )
  }
}
