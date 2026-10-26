package app.plugbrain.android.ui.challenges

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreen
import app.plugbrain.android.ui.challenges.presentation.ArithChallengeViewModel
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChallengeActivity : ComponentActivity() {
  private val challengeViewModel: ArithChallengeViewModel by viewModel()

  override fun onResume() {
    super.onResume()
    isInForeground = true
  }

  companion object {
    var isInForeground: Boolean = false
  }

  override fun onPause() {
    super.onPause()
    isInForeground = false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleBackButton()
    challengeViewModel.generateChallenge()

    enableEdgeToEdge()
    setContent {
      // Brand colors rather than the wallpaper's, so the screen matches the design in both themes.
      MathlockAppTheme(dynamicColor = false) {
        val challenge by challengeViewModel.challenge.collectAsState()
        val blockInterval by challengeViewModel.blockInterval.collectAsState()
        challenge?.let {
          when (it) {
            is NumericalChallenge -> NumericalChallengeScreen(
              modifier = Modifier.fillMaxSize(),
              challenge = it,
              unlockMinutes = blockInterval,
              onUnlockApp = {
                challengeViewModel.unblockApps(onUnblocked = ::finishAffinity)
              },
              onExitChallenge = exitToHomeScreen,
            )
            else -> Unit
          }
        }
      }
    }
  }

  private val exitToHomeScreen: () -> Unit = {
    val intent = Intent(Intent.ACTION_MAIN).apply {
      addCategory(Intent.CATEGORY_HOME)
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
    finishAndRemoveTask()
  }

  private fun handleBackButton() {
    onBackPressedDispatcher.addCallback(
      this,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          // Intentionally do nothing
        }
      },
    )
  }
}
