package app.plugbrain.android.ui.permissions

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri
import timber.log.Timber

// The system screens where the user grants each permission PlugBrain needs.

// Newer Android versions open PlugBrain's own usage access page when given its package. Older
// ones, and some manufacturers, have no such page, so they get the list of apps instead.
fun Context.openUsageAccessSettings() {
  try {
    startActivity(
      Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS, "package:$packageName".toUri()).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      },
    )
  } catch (e: ActivityNotFoundException) {
    Timber.d(e, "No usage access page for this app, opening the list")
    startActivity(
      Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      },
    )
  }
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

// Play allows this direct request for apps whose core feature breaks under battery optimization:
// PlugBrain has to keep running in the background to block apps.
@SuppressLint("BatteryLife")
fun Context.openBatteryOptimizationSettings() {
  startActivity(
    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, "package:$packageName".toUri()).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    },
  )
}
