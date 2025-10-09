package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
  onExitChallenge: () -> Unit,
) {
  Scaffold {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier
        .padding(it)
        .fillMaxSize(),
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        if (BuildConfig.DEBUG) {
          Text(
            text = challenge.difficultyLevel.toString(),
            style = MaterialTheme.typography.titleSmall,
          )
        }
        ChallengeHeader()
        if (isPortrait()) {
          Content(
            challenge = challenge,
            triggerAnimation = triggerAnimation,
            checkAnswer = checkAnswer,
          )
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

      Spacer(modifier = Modifier.weight(1f))

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 24.dp),
      ) {
        Button(
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
          onClick = onExitChallenge,
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
          )
          Text("Take me out of here")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "This will take you to your phone's home screen.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
  NumericalInputView(modifier = Modifier.fillMaxWidth(), checkAnswer = checkAnswer)
}

@Preview
@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Composable
private fun NumericalScreenPreview() {
  NumericalChallengeScreen(
    challenge = AdditionTwoDigitsCarryFreeChallenge(),
    triggerAnimation = false,
    checkAnswer = {},
    onExitChallenge = {},
  )
}
