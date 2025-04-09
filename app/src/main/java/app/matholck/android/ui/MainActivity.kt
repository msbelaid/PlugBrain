package app.matholck.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.matholck.android.ui.selectapps.AppsSelectionScreen
import app.matholck.android.ui.selectapps.presentation.AppsSelectionViewModel
import app.matholck.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
  private val installedAppsViewModel: AppsSelectionViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val installedApps = installedAppsViewModel.getInstalledApps()

    setContent {
      val selectedApps by installedAppsViewModel.blockedApps.collectAsState(emptySet())
      MathlockAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          AppsSelectionScreen(
            modifier = Modifier.padding(innerPadding),
            installedApps = installedApps,
            selectedApps = setOf(""),
            onItemClicked = { clickedPackage ->
              if (clickedPackage in selectedApps) {
                installedAppsViewModel.unblockApp(clickedPackage)
              } else {
                installedAppsViewModel.blockApp(clickedPackage)
              }
            },
          )
        }
      }
    }
  }
}
