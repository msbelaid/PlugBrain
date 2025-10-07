package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.BuildConfig
import app.plugbrain.android.challenges.MissingChallenge
import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.TwoOperandsChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsCarryFreeChallenge
import app.plugbrain.android.challenges.square.SquareChallenge
import app.plugbrain.android.ui.isPortrait

@Composable
fun NumericalChallengeScreen(
  modifier: Modifier = Modifier,
  challenge: NumericalChallenge,
  triggerAnimation: Boolean = true,
  checkAnswer: (Int?) -> Unit,
) {
  Scaffold {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = modifier.padding(it),
    ) {
      if (BuildConfig.DEBUG) {
        Text(
          text = challenge.difficultyLevel.toString(),
          style = MaterialTheme.typography.titleSmall,
        )
      }
      ChallengeHeader()
      if (isPortrait()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Content(
            challenge = challenge,
            triggerAnimation = triggerAnimation,
            checkAnswer = checkAnswer,
          )
        }
      } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Content(
            challenge = challenge,
            triggerAnimation = triggerAnimation,
            checkAnswer = checkAnswer,
          )
        }
      }
    }
  }
}

@Composable
private fun Content(
  challenge: NumericalChallenge,
  checkAnswer: (Int?) -> Unit,
  triggerAnimation: Boolean,
) {
  when (challenge) {
    is TwoOperandsChallenge -> TwoOperandsChallengeView(
      modifier = Modifier.padding(16.dp),
      challenge = challenge,
      triggerAnimation = triggerAnimation,
    )
    is MissingChallenge -> MissingChallengeView(
      modifier = Modifier.padding(16.dp),
      challenge = challenge,
      triggerAnimation = triggerAnimation,
    )
    is SquareChallenge -> SquareChallengeView(
      modifier = Modifier.padding(16.dp),
      challenge = challenge,
      triggerAnimation = triggerAnimation,
    )
    // Placeholder for generated challenges, do not remove
  }
  NumericalInputView(checkAnswer)
}

@Preview
@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Composable
private fun NumericalScreenPreview() {
  NumericalChallengeScreen(
    challenge = AdditionTwoDigitsCarryFreeChallenge(),
    triggerAnimation = false,
    checkAnswer = {},
  )
}
