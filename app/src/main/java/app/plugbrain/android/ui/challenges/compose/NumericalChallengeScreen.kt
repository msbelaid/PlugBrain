package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.BuildConfig
import app.plugbrain.android.R
import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsCarryFreeChallenge
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonPrimary
import app.plugbrain.android.ui.designsystem.components.card.PlugCard
import app.plugbrain.android.ui.theme.MathlockAppTheme

@Composable
fun NumericalChallengeScreen(
  modifier: Modifier = Modifier,
  challenge: NumericalChallenge,
  checkAnswer: (Int?) -> Unit,
  onExitChallenge: () -> Unit,
) {
  var answer by rememberSaveable(challenge) { mutableStateOf("") }

  Scaffold(modifier = modifier) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .consumeWindowInsets(innerPadding)
        .imePadding()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 32.dp, vertical = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      // Debug builds only: the challenge is picked at the user's current progressive difficulty.
      if (BuildConfig.DEBUG) {
        Text(
          text = "Difficulty: ${challenge.difficultyLevel}",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Spacer(Modifier.height(24.dp))
      Image(
        painter = painterResource(R.drawable.ic_launcher),
        contentDescription = null,
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(12.dp)),
      )
      Spacer(Modifier.height(16.dp))
      Text(
        text = stringResource(R.string.challenge_title),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
      )
      Spacer(Modifier.height(56.dp))
      Column(
        modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(40.dp),
      ) {
        PlugCard(
          challengeText = challenge.string(),
          inputValue = answer,
          onInputChange = { answer = it },
          onCheckAnswer = checkAnswer,
          placeholder = stringResource(R.string.challenge_answer_placeholder),
          ctaText = stringResource(R.string.challenge_check_answer),
          modifier = Modifier.fillMaxWidth(),
        )
        PlugButtonPrimary(
          text = stringResource(R.string.challenge_leave_app),
          onClick = onExitChallenge,
          icon = Icons.AutoMirrored.Filled.ExitToApp,
          modifier = Modifier.fillMaxWidth(),
        )
      }
    }
  }
}

@Preview(showSystemUi = true)
@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Composable
private fun NumericalScreenPreview() {
  MathlockAppTheme(dynamicColor = false) {
    NumericalChallengeScreen(
      challenge = AdditionTwoDigitsCarryFreeChallenge(),
      checkAnswer = {},
      onExitChallenge = {},
    )
  }
}
