package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.challenges.MissingChallenge
import app.plugbrain.android.challenges.missing.MissingAdditionChallenge
import app.plugbrain.android.challenges.missing.MissingDivisionChallenge
import app.plugbrain.android.challenges.missing.MissingMultiplicationChallenge
import app.plugbrain.android.challenges.missing.MissingSubtractionChallenge

@Composable
fun MissingChallengeView(
  modifier: Modifier = Modifier,
  challenge: MissingChallenge,
  triggerAnimation: Boolean = true,
) {
  val equation = challenge.string().split(" ")

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    equation.forEachIndexed { index, part ->
      AnimatedText(
        text = part,
        style = MaterialTheme.typography.displayLarge,
        delayMillis = index * 150L,
        triggerAnimation = triggerAnimation,
      )
    }
  }
}

@Preview
@Composable
private fun MissingAdditionChallengePreview() {
  MissingChallengeView(
    challenge = MissingAdditionChallenge(),
    triggerAnimation = false,
  )
}

@Preview
@Composable
private fun MissingSubtractionChallengePreview() {
  MissingChallengeView(
    challenge = MissingSubtractionChallenge(),
    triggerAnimation = false,
  )
}

@Preview
@Composable
private fun MissingMultiplicationChallengePreview() {
  MissingChallengeView(
    challenge = MissingMultiplicationChallenge(),
    triggerAnimation = false,
  )
}

@Preview
@Composable
private fun MissingDivisionChallengePreview() {
  MissingChallengeView(
    challenge = MissingDivisionChallenge(),
    triggerAnimation = false,
  )
}