package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.BuildConfig
import app.plugbrain.android.R
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
  Scaffold(
    modifier = Modifier.imePadding(),
    bottomBar = {
      Box(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
          .padding(
            paddingValues = WindowInsets.navigationBars.asPaddingValues(),
          ),
        contentAlignment = Alignment.Center,
      ) {
        ElevatedButton(
          modifier = Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
          onClick = onExitChallenge,
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
          ),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
          )
          Text(stringResource(R.string.take_me_out_of_here))
        }
      }
    },
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding),
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
