package app.matholck.android.ui.challenges

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
import app.matholck.android.ui.challenges.presentation.ArithChallengeViewModel
import app.matholck.android.ui.theme.MathlockAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChallengeActivity : ComponentActivity() {
  private val challengeViewModel: ArithChallengeViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MathlockAppTheme {
        val challenge by challengeViewModel.mathChallenge.collectAsState()

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          challenge?.let {
            ArithChallengeScreen(
              modifier = Modifier.padding(innerPadding),
              mathChallenge = it,
            ) { response ->
              if (it.checkAnswer(response)) {
                challengeViewModel.unblockApps()
                finish()
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
