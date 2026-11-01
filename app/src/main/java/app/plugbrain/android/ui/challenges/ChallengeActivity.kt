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
import app.plugbrain.android.ui.theme.PlugBrainTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChallengeActivity : ComponentActivity() {
  private val challengeViewModel: ArithChallengeViewModel by viewModel()

  override fun onResume() {
    super.onResume()
    // Opened straight into a split pane: the blocked app may be in the other one.
    if (isInMultiWindowMode) leaveSplitScreen() else isCoveringScreen = true
  }

  companion object {
    /**
     * Whether the challenge fills the screen. In split screen both panes are resumed, so being
     * resumed alone would let the blocked app be used next to the challenge (#94).
     */
    var isCoveringScreen: Boolean = false
      private set

    private val leftFullScreenEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    /** Emits when the challenge was put in split screen or a pop-up window, and closed itself. */
    val leftFullScreen: SharedFlow<Unit> = leftFullScreenEvents
  }

  override fun onPause() {
    super.onPause()
    isCoveringScreen = false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleBackButton()
    // resizeableActivity="false" doesn't stop this on tablets, foldables or Samsung's
    // "multi window for all apps", so close and let MathLockService reopen it full screen.
    addOnMultiWindowModeChangedListener { info ->
      if (info.isInMultiWindowMode) leaveSplitScreen()
    }
    challengeViewModel.generateChallenge()

    enableEdgeToEdge()
    setContent {
      // Brand colors rather than the wallpaper's, so the screen matches the design in both themes.
      PlugBrainTheme(dynamicColor = false) {
        val challenge by challengeViewModel.challenge.collectAsState()
        val blockInterval by challengeViewModel.blockInterval.collectAsState()
        challenge?.let {
          when (it) {
            is NumericalChallenge -> NumericalChallengeScreen(
              modifier = Modifier.fillMaxSize(),
              challenge = it,
              unlockMinutes = blockInterval,
              onUnlockApp = {
                // Removes the task too, so the solved challenge can't be reopened from recents.
                challengeViewModel.unblockApps(onUnblocked = ::finishAndRemoveTask)
              },
              onExitChallenge = exitToHomeScreen,
            )
            else -> Unit
          }
        }
      }
    }
  }

  private fun leaveSplitScreen() {
    isCoveringScreen = false
    leftFullScreenEvents.tryEmit(Unit)
    finishAndRemoveTask()
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
