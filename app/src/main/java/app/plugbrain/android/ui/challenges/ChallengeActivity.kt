package app.plugbrain.android.ui.challenges

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.plugbrain.android.ui.challenges.compose.CountChallengeScreen
import app.plugbrain.android.ui.challenges.presentation.ArithChallengeViewModel
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChallengeActivity : ComponentActivity() {
  private val challengeViewModel: ArithChallengeViewModel by viewModel()

  override fun onResume() {
    super.onResume()
    challengeViewModel.generateCountChallenge()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MathlockAppTheme {
        val countValue by challengeViewModel.countChallenge.collectAsState()

        CountChallengeScreen(
          countValue,
        ) { filledValue ->
          if (countValue == filledValue) {
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
      }
    }
  }
}
