package app.matholck.android.ui.selectapps.presentation

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.matholck.android.ui.selectapps.model.InstalledApp
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.launch

class AppsSelectionViewModel(
  private val app: Application,
  private val dataStoreManager: DataStoreManager
) : ViewModel() {

  val blockedApps = dataStoreManager.getBlockedApps

  fun getInstalledApps() : List<InstalledApp> {
    val pm = app.packageManager
    return pm
      .getInstalledApplications(PackageManager.GET_META_DATA)
      .filter {
        (it.flags and ApplicationInfo.FLAG_SYSTEM == 0) || (it.packageName in defaultSystemApps)
      }
      .mapNotNull { appInfo ->
        InstalledApp(
          name = pm.getApplicationLabel(appInfo).toString(),
          packageName = appInfo.packageName,
          icon = appInfo.loadIcon(pm)
        )
      }.sortedBy { it.name }
  }


  fun blockApp(packageName: String) {
    viewModelScope.launch {
      dataStoreManager.blockApp(packageName)
    }
  }

  fun unblockApp(packageName: String) {
    viewModelScope.launch {
      dataStoreManager.unblockApp(packageName)
    }
  }

  companion object {
    val defaultSystemApps = listOf(
      "com.android.vending", // Google Play Store
      "com.google.android.gms", // Google Play Services
      "com.google.android.googlequicksearchbox", // Google Search
      "com.google.android.apps.googleassistant", // Google Assistant
      "com.android.chrome", // Google Chrome
      "com.google.android.apps.maps", // Google Maps
      "com.google.android.gm", // Gmail
      "com.google.android.apps.photos", // Google Photos
      "com.google.android.apps.docs", // Google Drive
      "com.google.android.calendar", // Google Calendar
      "com.google.android.contacts", // Google Contacts
      "com.google.android.apps.messaging", // Google Messages
      "com.google.android.dialer", // Google Phone
      "com.google.android.youtube", // YouTube
      "com.google.android.apps.youtube.music", // YouTube Music
      "com.google.android.keep", // Google Keep Notes
      "com.google.android.apps.tachyon", // Google Duo (Meet)
      "com.google.android.apps.docs.editors.docs", // Google Docs
      "com.google.android.apps.docs.editors.sheets", // Google Sheets
      "com.google.android.apps.docs.editors.slides", // Google Slides
      "com.google.android.music", // Google Play Music (deprecated)
      "com.android.systemui", // Android System UI
      "com.android.settings", // Android Settings
      "com.android.calculator2", // Android Calculator
      "com.android.deskclock", // Android Clock
      "com.android.camera", // Android Camera (some devices use com.google.android.GoogleCamera)
      "com.android.mms", // Android Messages
      "com.android.documentsui", // Android Files
      "com.android.inputmethod.latin", // Android Keyboard (AOSP)
      "com.google.android.apps.wallpaper" // Wallpapers
    )

  }
}
