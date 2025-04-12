package app.matholck.android.ui.settings.presentation

data class PermissionsState(
  val accessibilityPermission: Boolean = false,
  val usageStatsPermission: Boolean = false,
  val batteryOptimizationExemption: Boolean = false,
  val systemAlertWindow: Boolean = false,
)
