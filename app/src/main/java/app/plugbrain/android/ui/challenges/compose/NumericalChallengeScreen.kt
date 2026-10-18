package app.plugbrain.android.ui.challenges.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsCarryFreeChallenge
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonPrimary
import app.plugbrain.android.ui.designsystem.components.card.PlugCard
import app.plugbrain.android.ui.theme.MathlockAppTheme
import kotlinx.coroutines.delay

private enum class AnswerState { Idle, Wrong, Correct }

@Composable
fun NumericalChallengeScreen(
  modifier: Modifier = Modifier,
  challenge: NumericalChallenge,
  onCorrectAnswer: () -> Unit,
  onExitChallenge: () -> Unit,
) {
  var answer by rememberSaveable(challenge) { mutableStateOf("") }
  var answerState by remember(challenge) { mutableStateOf(AnswerState.Idle) }
  // Bumped on every wrong submission so submitting the same wrong answer shakes the card again.
  var wrongAttempts by remember(challenge) { mutableIntStateOf(0) }

  val shakeOffset = remember { Animatable(0f) }
  val shakeDistance = with(LocalDensity.current) { 12.dp.toPx() }
  LaunchedEffect(wrongAttempts) {
    if (wrongAttempts == 0) return@LaunchedEffect
    shakeOffset.animateTo(
      targetValue = 0f,
      animationSpec = keyframes {
        durationMillis = SHAKE_DURATION_MILLIS
        -shakeDistance at 60
        shakeDistance at 120
        -shakeDistance * 0.6f at 180
        shakeDistance * 0.6f at 240
        -shakeDistance * 0.3f at 300
      },
    )
  }
  LaunchedEffect(answerState) {
    if (answerState == AnswerState.Correct) {
      // Let the success state show before the challenge screen goes away.
      delay(SUCCESS_HOLD_MILLIS)
      onCorrectAnswer()
    }
  }

  // Previews and screenshot tests render a single frame, so start fully visible there.
  val inspectionMode = LocalInspectionMode.current
  val entrance = remember { MutableTransitionState(inspectionMode) }
  LaunchedEffect(Unit) { entrance.targetState = true }

  Scaffold(modifier = modifier) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .consumeWindowInsets(innerPadding),
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .imePadding()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(Modifier.height(40.dp))
        EntranceItem(entrance, order = 0) { AppIcon() }
        Spacer(Modifier.height(16.dp))
        EntranceItem(entrance, order = 1) {
          Text(
            text = stringResource(R.string.challenge_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
          )
        }
        Spacer(Modifier.height(24.dp))
        Column(
          modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
          EntranceItem(entrance, order = 2) {
            PlugCard(
              challengeText = challenge.string(),
              inputValue = answer,
              onInputChange = {
                answer = it
                if (answerState == AnswerState.Wrong) answerState = AnswerState.Idle
              },
              onCheckAnswer = { response ->
                if (answerState != AnswerState.Correct) {
                  if (response != null && challenge.checkAnswer(response)) {
                    answerState = AnswerState.Correct
                  } else {
                    answerState = AnswerState.Wrong
                    wrongAttempts++
                  }
                }
              },
              placeholder = stringResource(R.string.challenge_answer_placeholder),
              ctaText = stringResource(R.string.challenge_check_answer),
              isSuccess = answerState == AnswerState.Correct,
              isError = answerState == AnswerState.Wrong,
              modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { translationX = shakeOffset.value },
            )
          }
          EntranceItem(entrance, order = 3) {
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
  }
}

// Fades and lifts each block in one after the other, to set a calmer pace.
@Composable
private fun EntranceItem(
  state: MutableTransitionState<Boolean>,
  order: Int,
  content: @Composable () -> Unit,
) {
  val delayMillis = order * ENTRANCE_STAGGER_MILLIS
  AnimatedVisibility(
    visibleState = state,
    enter = fadeIn(tween(ENTRANCE_DURATION_MILLIS, delayMillis)) +
      slideInVertically(tween(ENTRANCE_DURATION_MILLIS, delayMillis)) { it / 4 },
  ) {
    content()
  }
}

// The launcher drawables are adaptive icon layers (108dp with a 72dp safe zone),
// so they are stacked and cropped to the visible area, like a launcher would.
@Composable
private fun AppIcon() {
  Box(
    modifier = Modifier
      .size(AppIconSize)
      .clip(RoundedCornerShape(16.dp)),
    contentAlignment = Alignment.Center,
  ) {
    Image(
      painter = painterResource(R.drawable.ic_launcher_background),
      contentDescription = null,
      modifier = Modifier.requiredSize(AppIconSize * 1.5f),
    )
    Image(
      painter = painterResource(R.drawable.ic_launcher),
      contentDescription = null,
      modifier = Modifier.requiredSize(AppIconSize * 1.5f),
    )
  }
}

private val AppIconSize = 64.dp
private const val ENTRANCE_DURATION_MILLIS = 500
private const val ENTRANCE_STAGGER_MILLIS = 120
private const val SHAKE_DURATION_MILLIS = 360
private const val SUCCESS_HOLD_MILLIS = 600L

@Preview(showSystemUi = true)
@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Composable
private fun NumericalScreenPreview() {
  MathlockAppTheme(dynamicColor = false) {
    NumericalChallengeScreen(
      challenge = AdditionTwoDigitsCarryFreeChallenge(),
      onCorrectAnswer = {},
      onExitChallenge = {},
    )
  }
}
