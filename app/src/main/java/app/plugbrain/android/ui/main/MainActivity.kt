package app.plugbrain.android.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.main.compose.MainScreen
import app.plugbrain.android.ui.main.presentation.MainScreenState
import app.plugbrain.android.ui.main.presentation.MainScreenViewModel
import app.plugbrain.android.ui.settings.SettingsActivity
import app.plugbrain.android.ui.theme.MathlockAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
  private val mainScreenViewModel: MainScreenViewModel by viewModel()

  override fun onResume() {
    super.onResume()
    mainScreenViewModel.getPermissions()
    mainScreenViewModel.getAppsUsageStats()
    lifecycleScope.launch {
      mainScreenViewModel.permissionsState.collect { permissions ->
        if (permissions.areAllImportantPermissionsGranted().not()) {
          navigateToSettings()
        }
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val state by mainScreenViewModel.mainScreenState.collectAsState(MainScreenState())
      val permissionsState by mainScreenViewModel.permissionsState.collectAsState(PermissionsState())
      MathlockAppTheme {
        MainScreen(
          state = state,
          permissionsState = permissionsState,
          onSettingsClicked = { navigateToSettings() },
        )
      }
    }
  }

  private fun navigateToSettings() {
    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
  }
}
