package app.plugbrain.android.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import app.plugbrain.android.R
import app.plugbrain.android.repository.model.InstalledApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class InstalledAppsRepository(private val context: Context) {
  fun getInstalledApps(): Flow<List<InstalledApp>> = flow {
    val pm = context.packageManager
    val apps = pm
      .getInstalledApplications(PackageManager.GET_META_DATA)
      .filter {
        (it.flags and ApplicationInfo.FLAG_SYSTEM == 0) || (it.packageName in context.resources.getStringArray(R.array.default_system_apps))
      }
      .map { appInfo ->
        InstalledApp(
          name = pm.getApplicationLabel(appInfo).toString(),
          packageName = appInfo.packageName,
          icon = appInfo.loadIcon(pm),
        )
      }.sortedBy { it.name }
    emit(apps)
  }.flowOn(Dispatchers.IO)
}
