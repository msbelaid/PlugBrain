package app.plugbrain.android.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import app.plugbrain.android.ui.main.compose.MainScreen
import app.plugbrain.android.ui.main.presentation.MainScreenViewModel
import app.plugbrain.android.ui.onboarding.OnboardingActivity
import app.plugbrain.android.ui.selectapps.AppsSelectionActivity
import app.plugbrain.android.ui.settings.SettingsActivity
import app.plugbrain.android.ui.theme.MathlockAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
  private val mainScreenViewModel: MainScreenViewModel by viewModel()

  // Set once we know onboarding is done; until then nothing is shown or refreshed.
  private var isReady = false

  override fun onResume() {
    super.onResume()
    if (isReady) mainScreenViewModel.refresh()
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
      val state by mainScreenViewModel.mainScreenState.collectAsState()
      MathlockAppTheme {
        MainScreen(
          state = state,
          // Settings holds the permission explanations and the interval and difficulty pickers.
          onSettingsClicked = { navigateTo(SettingsActivity::class.java) },
          onAppsClicked = { navigateTo(AppsSelectionActivity::class.java) },
        )
      }
    }
    mainScreenViewModel.refresh()
  }

  private fun navigateTo(activity: Class<*>) {
    startActivity(Intent(this, activity))
  }
}
