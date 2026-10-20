package app.plugbrain.android.repository.model

data class PermissionsState(
  val accessibilityPermission: Boolean = false,
  val usageStatsPermission: Boolean = false,
  val batteryOptimizationExemption: Boolean = false,
  val systemAlertWindow: Boolean = false,
  // Null on phones that have no such setting, so it is neither shown nor required there.
  val backgroundPopups: Boolean? = null,
) {
  fun areAllPermissionsGranted(): Boolean =
    accessibilityPermission &&
      usageStatsPermission &&
      batteryOptimizationExemption &&
      systemAlertWindow &&
      backgroundPopups != false

  fun areAllImportantPermissionsGranted(): Boolean =
    accessibilityPermission &&
      usageStatsPermission &&
      systemAlertWindow &&
      backgroundPopups != false
}
