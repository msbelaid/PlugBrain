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
import app.plugbrain.android.ui.challenges.presentation.ArithChallengeViewModel
import app.plugbrain.android.ui.theme.MathlockAppTheme
import app.plugbrain.android.ui.timer.TimerActivity
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
    enableEdgeToEdge()
    setContent {
      MathlockAppTheme {
        val challenge by challengeViewModel.mathChallenge.collectAsState()
        Scaffold(
          topBar = {
            if (BuildConfig.DEBUG) { // The refresh button works only on DEBUG mode!
              ChallengeTopBar(onRefreshClicked = {
                startActivity(Intent(this@ChallengeActivity, TimerActivity::class.java))
              })
            }
          },
          modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
          challenge?.let {
            ArithChallengeScreen(
              modifier = Modifier.padding(innerPadding),
              mathChallenge = it,
            ) { response ->
              if (it.checkAnswer(response)) {
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
  }
}
