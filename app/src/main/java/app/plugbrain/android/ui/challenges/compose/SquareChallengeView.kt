package app.plugbrain.android.ui.challenges.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.plugbrain.android.challenges.square.SquareChallenge
import app.plugbrain.android.challenges.square.SquareChallengeMediumSixThroughTen

@Composable
fun SquareChallengeView(
  modifier: Modifier = Modifier,
  challenge: SquareChallenge,
  triggerAnimation: Boolean = true,
) {
  AnimatedText(
    modifier = modifier,
    text = challenge.string(),
    style = MaterialTheme.typography.displayLarge,
    delayMillis = 0,
    triggerAnimation = triggerAnimation,
  )
}

@Preview
@Composable
private fun SquareChallengeViewPreview() {
  SquareChallengeView(
    challenge = SquareChallengeMediumSixThroughTen(),
    triggerAnimation = false,
  )
}
