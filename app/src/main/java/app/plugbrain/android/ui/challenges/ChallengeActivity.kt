package app.plugbrain.android.ui.challenges

import android.os.Bundle
import android.widget.Toast
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
    challengeViewModel.generateChallenge()
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

    enableEdgeToEdge()
    setContent {
      MathlockAppTheme {
        val challenge by challengeViewModel.challenge.collectAsState()
        challenge?.let {
          when (it) {
            is NumericalChallenge -> NumericalChallengeScreen(
              modifier = Modifier.fillMaxSize(),
              challenge = it,
            ) { response ->
              checkNumericalAnswer(it, response)
            }
            else -> Unit
          }
        }
      }
    }
  }

  private fun checkNumericalAnswer(
    challenge: NumericalChallenge,
    response: Int,
  ) {
    if (challenge.checkAnswer(response)) {
      challengeViewModel.unblockApps()
      finishAffinity()
    } else {
      Toast.makeText(
        this@ChallengeActivity,
        "Wrong Answer, try again!",
        Toast.LENGTH_LONG,
      ).show()
    }
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
