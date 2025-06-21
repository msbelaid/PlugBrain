package app.plugbrain.android.repository.model

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

  fun areAllImportantPermissionsGranted(): Boolean =
    accessibilityPermission &&
      usageStatsPermission &&
      systemAlertWindow
}
