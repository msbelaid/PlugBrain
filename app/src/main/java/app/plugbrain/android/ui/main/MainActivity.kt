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
import app.plugbrain.android.ui.onboarding.OnboardingActivity
import app.plugbrain.android.ui.settings.SettingsActivity
import app.plugbrain.android.ui.theme.MathlockAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
  private val mainScreenViewModel: MainScreenViewModel by viewModel()

  // Set once we know onboarding is done; until then nothing is shown or refreshed.
  private var isReady = false

  // Missing permissions send the user to Settings once, so Back from Settings doesn't bounce them there again.
  private var hasRedirectedToSettings = false

  override fun onResume() {
    super.onResume()
    if (isReady) refresh()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    lifecycleScope.launch {
      if (mainScreenViewModel.needsOnboarding()) {
        startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
        finish()
        return@launch
      }
      showMainScreen()
    }
  }

  private fun showMainScreen() {
    isReady = true
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
    lifecycleScope.launch {
      mainScreenViewModel.permissionsState.collect { permissions ->
        if (permissions != null && permissions.areAllImportantPermissionsGranted().not() && !hasRedirectedToSettings) {
          hasRedirectedToSettings = true
          navigateToSettings()
        }
      }
    }
    refresh()
  }

  private fun refresh() {
    mainScreenViewModel.getPermissions()
    mainScreenViewModel.getAppsUsageStats()
  }

  private fun navigateToSettings() {
    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
  }
}
