package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.challenges.TwoOperandsChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationFourByFourDigitChallenge

@Composable
fun TwoOperandsChallengeView(
  modifier: Modifier = Modifier,
  challenge: TwoOperandsChallenge,
  triggerAnimation: Boolean = true,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    AnimatedText(
      text = challenge.operand1.toString(),
      style = MaterialTheme.typography.displayLarge,
      delayMillis = 0,
      triggerAnimation = triggerAnimation,
    )
    AnimatedText(
      text = challenge.operationType.symbol,
      style = MaterialTheme.typography.displayLarge,
      delayMillis = 150,
      triggerAnimation = triggerAnimation,
    )
    AnimatedText(
      text = challenge.operand2.toString(),
      style = MaterialTheme.typography.displayLarge,
      delayMillis = 300,
      triggerAnimation = triggerAnimation,
    )
  }
}

@Preview
@Composable
private fun ArithChallengePortraitScreenPreview() {
  TwoOperandsChallengeView(
    challenge = MultiplicationFourByFourDigitChallenge(),
    triggerAnimation = false,
  )
}
