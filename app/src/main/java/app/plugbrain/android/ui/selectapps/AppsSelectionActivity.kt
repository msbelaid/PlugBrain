package app.plugbrain.android.ui.selectapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.plugbrain.android.ui.selectapps.presentation.AppsSelectionViewModel
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppsSelectionActivity : ComponentActivity() {
  private val installedAppsViewModel: AppsSelectionViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val blockedApps by installedAppsViewModel.blockedAppsFlow.collectAsStateWithLifecycle()
      val installedApps by installedAppsViewModel.installedApps.collectAsStateWithLifecycle()
      val searchQuery by installedAppsViewModel.searchQuery.collectAsStateWithLifecycle()

      MathlockAppTheme {
        Scaffold { innerPadding ->
          AppsSelectionScreen(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding),
            blockedApps = blockedApps,
            installedAppsState = installedApps,
            onQueryChanged = installedAppsViewModel::onChangeQuery,
            searchQuery = searchQuery,
            onItemClicked = { clickedPackage, checked ->
              if (checked) {
                installedAppsViewModel.blockApp(clickedPackage)
              } else {
                installedAppsViewModel.unblockApp(clickedPackage)
              }
            },
          )
        }
      }
    }
  }
}
