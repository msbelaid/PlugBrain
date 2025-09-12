package app.plugbrain.android.ui.challenges.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.plugbrain.android.challenges.NumberChallenge
import app.plugbrain.android.challenges.TwoOperandsChallenge

@Composable
fun NumberChallengeScreen(
  modifier: Modifier = Modifier,
  challenge: NumberChallenge,
  checkAnswer: (Int) -> Unit,
) {
  when (challenge) {
    is TwoOperandsChallenge -> TwoOperandsChallengeScreen(
      modifier = modifier,
      challenge = challenge,
      checkAnswer = checkAnswer,
    )
    // Placeholder for generated challenges, do not remove
  }
}
