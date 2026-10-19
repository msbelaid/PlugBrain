package app.plugbrain.android.ui.permissions

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri

// The system screens where the user grants each permission PlugBrain needs.

fun Context.openUsageAccessSettings() {
  startActivity(
    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    },
  )
}

fun Context.openOverlaySettings() {
  startActivity(
    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "package:$packageName".toUri()).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    },
  )
}

fun Context.openAccessibilitySettings() {
  startActivity(
    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    },
  )
}

fun Context.openBatteryOptimizationSettings() {
  startActivity(
    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, "package:$packageName".toUri()).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    },
  )
}
