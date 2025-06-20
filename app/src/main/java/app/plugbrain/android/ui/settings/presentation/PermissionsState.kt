package app.plugbrain.android.ui.settings.presentation

data class PermissionsState(
  val accessibilityPermission: Boolean = false,
  val usageStatsPermission: Boolean = false,
  val batteryOptimizationExemption: Boolean = false,
  val systemAlertWindow: Boolean = false,
) {
  fun areAllPermissionsGranted(): Boolean =
    accessibilityPermission &&
      usageStatsPermission &&
      batteryOptimizationExemption &&
      systemAlertWindow
}
