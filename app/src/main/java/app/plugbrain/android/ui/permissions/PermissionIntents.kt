package app.plugbrain.android.ui.permissions

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

fun Context.openBatteryOptimizationSettings() {
  startActivity(
    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, "package:$packageName".toUri()).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    },
  )
}

// Xiaomi's own permission page for PlugBrain, where the background pop-ups setting lives. Phones
// without it get PlugBrain's app info page, whose "Other permissions" leads to the same setting.
fun Context.openBackgroundPopupsSettings() {
  try {
    startActivity(
      Intent("miui.intent.action.APP_PERM_EDITOR").apply {
        setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
        putExtra("extra_pkgname", packageName)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      },
    )
  } catch (e: RuntimeException) {
    // Missing, or not exported, depending on the MIUI version.
    Timber.d(e, "Can't open the Xiaomi permission page, opening the app info")
    startActivity(
      Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:$packageName".toUri()).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      },
    )
  }
}
