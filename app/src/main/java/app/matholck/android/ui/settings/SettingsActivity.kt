package app.matholck.android.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.matholck.android.ui.selectapps.AppsSelectionActivity
import app.matholck.android.ui.settings.presentation.SettingsViewModel
import app.matholck.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : ComponentActivity() {
  private val settingsViewModel: SettingsViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val lockedApps by settingsViewModel.getLockedApps().collectAsState(emptyList())
      MathlockAppTheme {
        SettingsScreen(
          lockedApps = lockedApps,
          onBlockApplicationsClicked = {
            startActivity(Intent(this@SettingsActivity, AppsSelectionActivity::class.java))
          },
        )
      }
    }
  }
}
