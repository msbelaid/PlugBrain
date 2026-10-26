package app.plugbrain.android.ui.main.presentation

import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.repository.model.PermissionsState

data class MainScreenState(
  val status: DashboardStatus,
  val blockedApps: List<InstalledApp> = emptyList(),
  val blockInterval: Int,
  /** Opening a blocked app is challenged too, not only [blockInterval] minutes of use. */
  val challengeOnAppOpen: Boolean = false,
  val minDifficulty: Int,
  /** True while Android may stop PlugBrain in the background to save battery; granting it is optional. */
  val isBatteryOptimized: Boolean = false,
)

/** A permission PlugBrain can't block apps without. */
enum class RequiredPermission {
  Accessibility,
  UsageAccess,
  Overlay,
}

/** What the status card at the top of the main screen says. */
sealed interface DashboardStatus {
  data class Paused(val missingPermission: RequiredPermission) : DashboardStatus

  data class NothingToBlock(val blockInterval: Int) : DashboardStatus

  /** [awayMinutes] is null when no blocked app was ever opened. */
  data class AppsLocked(val awayMinutes: Long?, val challengeLevel: Int) : DashboardStatus

  data class Active(
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
@Suppress("LongParameterList") // A pure mapping of the dashboard inputs, kept flat so tests can set each one.
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
    missingPermission != null -> DashboardStatus.Paused(missingPermission)
    !hasBlockedApps -> DashboardStatus.NothingToBlock(blockInterval)
    isBlocked -> DashboardStatus.AppsLocked(awayMinutes, challengeLevel)
    else -> DashboardStatus.Active(usedMinutes, blockInterval, challengeLevel, maxChallengeLevel)
  }
}
