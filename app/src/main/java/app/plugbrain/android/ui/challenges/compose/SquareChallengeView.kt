package app.plugbrain.android.ui.challenges.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.plugbrain.android.challenges.square.SquareChallenge
import app.plugbrain.android.challenges.square.SquareChallengeMediumSixThroughTen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.AnimatedText

//It seems I needed an API to use AnimatedText. So, I used this function for preview.
//@Composable
//fun AnimatedText(
//  text: String,
//  style: androidx.compose.ui.text.TextStyle,
//  delayMillis: Int,
//  triggerAnimation: Boolean,
//  modifier: Modifier = Modifier
//) {
//  Text(
//    text = text,
//    style = style,
//    modifier = modifier
//  )
//}



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
      text = challenge.string(),
      style = MaterialTheme.typography.displayLarge,
      delayMillis = 0,
      triggerAnimation = triggerAnimation,
    )
  }
}

@Preview
@Composable
private fun SquareChallengeViewPreview() {
  SquareChallengeView(
    challenge = SquareChallengeMediumSixThroughTen(),
    triggerAnimation = false,
  )
}
