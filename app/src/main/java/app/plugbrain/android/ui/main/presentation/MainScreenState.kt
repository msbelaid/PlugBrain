package app.plugbrain.android.ui.main.presentation

import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.repository.model.PermissionsState

data class MainScreenState(
  val status: DashboardStatus,
  val blockedApps: List<InstalledApp> = emptyList(),
  val blockInterval: Int,
  val minDifficulty: Int,
)

/** A permission PlugBrain can't block apps without. */
enum class RequiredPermission {
  Accessibility,
  UsageAccess,
  Overlay,
}

/** What the status card at the top of the main screen says. */
sealed interface DashboardStatus {
  data class ProtectionPaused(val missingPermission: RequiredPermission) : DashboardStatus

  data class NothingToBlock(val blockInterval: Int) : DashboardStatus

  /** [awayMinutes] is null when no blocked app was ever opened. */
  data class AppsLocked(val awayMinutes: Long?, val challengeLevel: Int) : DashboardStatus

  data class ProtectionOn(
    val usedMinutes: Long,
    val blockInterval: Int,
    val nextChallengeLevel: Int,
    val maxChallengeLevel: Int,
  ) : DashboardStatus {
    val minutesLeft: Long get() = (blockInterval - usedMinutes).coerceAtLeast(0)

    val usedFraction: Float get() = if (blockInterval > 0) (usedMinutes.toFloat() / blockInterval).coerceIn(0f, 1f) else 0f
  }
}

/** The first match wins: a missing permission, then no apps to block, then locked, then on. */
internal fun dashboardStatus(
  permissions: PermissionsState,
  hasBlockedApps: Boolean,
  isBlocked: Boolean,
  usedMinutes: Long,
  awayMinutes: Long?,
  blockInterval: Int,
  challengeLevel: Int,
  maxChallengeLevel: Int,
): DashboardStatus {
  val missingPermission = when {
    !permissions.accessibilityPermission -> RequiredPermission.Accessibility
    !permissions.usageStatsPermission -> RequiredPermission.UsageAccess
    !permissions.systemAlertWindow -> RequiredPermission.Overlay
    else -> null
  }
  return when {
    missingPermission != null -> DashboardStatus.ProtectionPaused(missingPermission)
    !hasBlockedApps -> DashboardStatus.NothingToBlock(blockInterval)
    isBlocked -> DashboardStatus.AppsLocked(awayMinutes, challengeLevel)
    else -> DashboardStatus.ProtectionOn(usedMinutes, blockInterval, challengeLevel, maxChallengeLevel)
  }
}
