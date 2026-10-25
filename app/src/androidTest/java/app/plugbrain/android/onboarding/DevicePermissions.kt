package app.plugbrain.android.onboarding

import androidx.test.uiautomator.UiDevice
import app.plugbrain.android.service.MathLockService

/**
 * Grants and revokes PlugBrain's special permissions with adb shell rights, so a test can do in
 * one command what a user does in the system settings. Each one changes exactly what
 * PermissionsRepository reads.
 */
class DevicePermissions(
    private val device: UiDevice,
    private val packageName: String,
) {
    private val accessibilityService = "$packageName/${MathLockService::class.qualifiedName}"

    fun grantUsageAccess() {
        shell("appops set $packageName GET_USAGE_STATS allow")
    }

    fun grantOverlay() {
        shell("appops set $packageName SYSTEM_ALERT_WINDOW allow")
    }

    fun grantAccessibility() {
        shell("settings put secure enabled_accessibility_services $accessibilityService")
        shell("settings put secure accessibility_enabled 1")
    }

    fun grantBatteryExemption() {
        shell("cmd deviceidle whitelist +$packageName")
    }

    fun isAccessibilityEnabled(): Boolean = shell("settings get secure enabled_accessibility_services").contains(accessibilityService)

    fun revokeAll() {
        shell("appops set $packageName GET_USAGE_STATS default")
        shell("appops set $packageName SYSTEM_ALERT_WINDOW deny")
        // The test emulator has no other accessibility service, so nothing else gets turned off.
        shell("settings delete secure enabled_accessibility_services")
        shell("cmd deviceidle whitelist -$packageName")
    }

    private fun shell(command: String): String = device.executeShellCommand(command)
}
