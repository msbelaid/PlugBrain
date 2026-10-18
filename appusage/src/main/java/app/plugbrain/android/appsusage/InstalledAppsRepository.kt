package app.plugbrain.android.appsusage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import app.plugbrain.android.appsusage.model.InstalledApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class InstalledAppsRepository(private val context: Context) {

  private fun isAppVisible(appInfo: ApplicationInfo, launchablePackages: Set<String>): Boolean {
    // Filter out system apps, but keep a specific allow-list
    val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
    val isAllowedSystemApp = appInfo.packageName in
      context.resources.getStringArray(R.array.default_system_apps)
    // System apps with a launcher icon are user-facing (e.g. a preinstalled browser
    // like Vanadium on GrapheneOS), so they can be distracting too.
    val isLaunchable = appInfo.packageName in launchablePackages

    // Keep the app if it's not a system app, OR if it's an allowed or launchable system app.
    // Also, always filter out our own app.
    return (isAllowedSystemApp || isLaunchable || !isSystemApp) &&
      appInfo.packageName != context.packageName
  }

  private fun getLaunchablePackages(pm: PackageManager): Set<String> {
    val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
    return pm.queryIntentActivities(launcherIntent, 0)
      .map { it.activityInfo.packageName }
      .toSet()
  }

  fun getInstalledApps(): Flow<List<InstalledApp>> = callbackFlow {
    val pm = context.packageManager

    // Function to fetch and send the app list
    fun sendApps() {
      val launchablePackages = getLaunchablePackages(pm)
      val apps = pm
        .getInstalledApplications(PackageManager.GET_META_DATA)
        .filter { isAppVisible(it, launchablePackages) }
        .map { appInfo ->
          InstalledApp(
            name = pm.getApplicationLabel(appInfo).toString(),
            packageName = appInfo.packageName,
            icon = appInfo.loadIcon(pm),
          )
        }.sortedBy { it.name.lowercase() }
      trySend(apps)
    }

    // Create a broadcast receiver to listen for app changes
    val broadcastReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        // When an app is installed or uninstalled, refresh our list
        sendApps()
      }
    }

    // Register the receiver
    val intentFilter = IntentFilter().apply {
      addAction(Intent.ACTION_PACKAGE_ADDED)
      addAction(Intent.ACTION_PACKAGE_REMOVED)
      addDataScheme("package")
    }
    context.registerReceiver(broadcastReceiver, intentFilter)

    // Send the initial list of apps
    sendApps()

    // must unregister the receiver to avoid memory leaks.
    awaitClose {
      context.unregisterReceiver(broadcastReceiver)
    }
  }.flowOn(Dispatchers.IO)
}
