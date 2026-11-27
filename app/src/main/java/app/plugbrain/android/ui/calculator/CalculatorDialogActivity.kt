package app.plugbrain.android.ui.calculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.plugbrain.android.ui.challenges.ChallengeActivity
import app.plugbrain.android.ui.theme.PlugBrainTheme

/**
 * Shows [CalculatorDialog] over the calculator. It runs in its own task, so opening it doesn't
 * bring the challenge's task to the front and the calculator stays visible behind it.
 */
class CalculatorDialogActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      PlugBrainTheme(dynamicColor = false) {
        CalculatorDialog(
          onBackToChallenge = ::backToChallenge,
          onUseCalculator = ::finishAndRemoveTask,
        )
      }
    }
  }

  private fun backToChallenge() {
    // The challenge is a single task, so this brings back the same problem instead of a new one.
    startActivity(
      Intent(this, ChallengeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
    )
    finishAndRemoveTask()
  }
}
