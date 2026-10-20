package app.plugbrain.android.main

import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.main.presentation.DashboardStatus
import app.plugbrain.android.ui.main.presentation.RequiredPermission
import app.plugbrain.android.ui.main.presentation.dashboardStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardStatusTest {
    private val allGranted =
        PermissionsState(
            accessibilityPermission = true,
            usageStatsPermission = true,
            systemAlertWindow = true,
        )

    private fun status(
        permissions: PermissionsState = allGranted,
        hasBlockedApps: Boolean = true,
        isBlocked: Boolean = false,
        usedMinutes: Long = 4,
        awayMinutes: Long? = 6,
    ) = dashboardStatus(
        permissions = permissions,
        hasBlockedApps = hasBlockedApps,
        isBlocked = isBlocked,
        usedMinutes = usedMinutes,
        awayMinutes = awayMinutes,
        blockInterval = 15,
        challengeLevel = 4,
        maxChallengeLevel = 15,
    )

    @Test
    fun `a missing permission pauses PlugBrain whatever else is true`() {
        assertEquals(
            DashboardStatus.Paused(RequiredPermission.Accessibility),
            status(permissions = allGranted.copy(accessibilityPermission = false), hasBlockedApps = false, isBlocked = true),
        )
        assertEquals(
            DashboardStatus.Paused(RequiredPermission.UsageAccess),
            status(permissions = allGranted.copy(usageStatsPermission = false)),
        )
        assertEquals(
            DashboardStatus.Paused(RequiredPermission.Overlay),
            status(permissions = allGranted.copy(systemAlertWindow = false)),
        )
        assertEquals(
            DashboardStatus.Paused(RequiredPermission.BackgroundPopups),
            status(permissions = allGranted.copy(backgroundPopups = false)),
        )
    }

    @Test
    fun `phones without the background pop-ups setting are not paused`() {
        assertEquals(
            DashboardStatus.Active(usedMinutes = 4, blockInterval = 15, nextChallengeLevel = 4, maxChallengeLevel = 15),
            status(permissions = allGranted.copy(backgroundPopups = null)),
        )
    }

    @Test
    fun `battery optimisation alone does not pause PlugBrain`() {
        assertEquals(
            DashboardStatus.Active(usedMinutes = 4, blockInterval = 15, nextChallengeLevel = 4, maxChallengeLevel = 15),
            status(permissions = allGranted.copy(batteryOptimizationExemption = false)),
        )
    }

    @Test
    fun `no blocked apps asks the user to choose some`() {
        assertEquals(DashboardStatus.NothingToBlock(blockInterval = 15), status(hasBlockedApps = false, isBlocked = true))
    }

    @Test
    fun `blocked apps show how long the user stayed away`() {
        assertEquals(DashboardStatus.AppsLocked(awayMinutes = 6, challengeLevel = 4), status(isBlocked = true))
        assertEquals(DashboardStatus.AppsLocked(awayMinutes = null, challengeLevel = 4), status(isBlocked = true, awayMinutes = null))
    }

    @Test
    fun `the active state counts the minutes left and never goes below zero`() {
        val on = status(usedMinutes = 4) as DashboardStatus.Active
        assertEquals(11L, on.minutesLeft)
        assertEquals(4f / 15f, on.usedFraction, 0.001f)

        val over = status(usedMinutes = 20) as DashboardStatus.Active
        assertEquals(0L, over.minutesLeft)
        assertEquals(1f, over.usedFraction, 0.001f)
    }
}
