package app.plugbrain.android.ui.challenges

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.plugbrain.android.BuildConfig
import app.plugbrain.android.ui.challenges.compose.ArithChallengeScreen
import app.plugbrain.android.ui.challenges.compose.ChallengeTopBar
import app.plugbrain.android.ui.challenges.compose.CountChallengeScreen
import app.plugbrain.android.ui.challenges.presentation.ArithChallengeViewModel
import app.plugbrain.android.ui.theme.MathlockAppTheme
import app.plugbrain.android.ui.timer.TimerActivity
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
        val challenge by challengeViewModel.mathChallenge.collectAsState()
        val countValue = challengeViewModel.countChallenge

        CountChallengeScreen(
          countValue,
        ) { filledValue ->
          if (countValue == filledValue)  {
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
