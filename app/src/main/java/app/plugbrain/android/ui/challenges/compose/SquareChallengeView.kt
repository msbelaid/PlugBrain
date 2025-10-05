package app.plugbrain.android.ui.challenges.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.plugbrain.android.challenges.square.SquareChallenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.AnimatedText

@Composable
fun SquareChallengeView(
  modifier: Modifier = Modifier,
  challenge: SquareChallenge,
  triggerAnimation: Boolean = true
) {

Row(
modifier = modifier,
horizontalArrangement = Arrangement.spacedBy(16.dp),
) {
  AnimatedText(
    text = challenge.operand.toString(),
    style = MaterialTheme.typography.displayLarge,
    delayMillis = 0,
    triggerAnimation = triggerAnimation,
  )
  AnimatedText(
    text = challenge.symbol,
    style = MaterialTheme.typography.displayLarge,
    delayMillis = 150,
    triggerAnimation = triggerAnimation,
  )
  AnimatedText(
    text = challenge.operand.toString(),
    style = MaterialTheme.typography.displayLarge,
    delayMillis = 300,
    triggerAnimation = triggerAnimation,
  )
}
}

//@Preview
//@Composable
//private fun SquareChallengeViewPreview() {
//  SquareChallengeView(
//    challenge = MultiplicationFourByFourDigitChallenge(),
//    triggerAnimation = false,
//  )
//}
