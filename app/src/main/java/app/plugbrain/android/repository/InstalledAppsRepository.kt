package app.plugbrain.android.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import app.plugbrain.android.R
import app.plugbrain.android.repository.model.InstalledApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class InstalledAppsRepository(private val context: Context) {

  private fun isAppVisible(appInfo: ApplicationInfo): Boolean {
    // Filter out system apps, but keep a specific allow-list
    val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
    val isAllowedSystemApp = appInfo.packageName in
      context.resources.getStringArray(R.array.default_system_apps)

    // Keep the app if it's not a system app, OR if it's an allowed system app.
    // Also, always filter out our own app.
    return (isAllowedSystemApp || !isSystemApp) && appInfo.packageName != context.packageName
  }

  fun getInstalledApps(): Flow<List<InstalledApp>> = callbackFlow {
    val pm = context.packageManager

    // Function to fetch and send the app list
    fun sendApps() {
      val apps = pm
        .getInstalledApplications(PackageManager.GET_META_DATA)
        .filter { isAppVisible(it) }
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
