package app.matholck.android.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.matholck.android.ui.selectapps.AppsSelectionActivity
import app.matholck.android.ui.selectapps.presentation.AppsSelectionViewModel
import app.matholck.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : ComponentActivity() {
  private val installedAppsViewModel: AppsSelectionViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val installedApps by installedAppsViewModel.getInstalledApps().collectAsState(emptyList())
      val blockedApps by installedAppsViewModel.blockedApps.collectAsState(emptySet())
      MathlockAppTheme {
        SettingsScreen(
          installedApps = installedApps,
          blockedApps = blockedApps,
          onBlockApplicationsClicked = {
            startActivity(Intent(this@SettingsActivity, AppsSelectionActivity::class.java))
          }
        )
      }
    }
  }
}
