package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.challenges.NumericalChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsCarryFreeChallenge
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreenDefaults.ContentMaxWidth
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreenDefaults.ContentSpacing
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreenDefaults.HeaderBottomSpacing
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreenDefaults.HeaderTopSpacing
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreenDefaults.HorizontalPadding
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreenDefaults.VerticalPadding
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonPrimary
import app.plugbrain.android.ui.designsystem.components.card.PlugCard
import app.plugbrain.android.ui.theme.PlugBrainTheme
import kotlinx.coroutines.delay

// Lets the success state on the card register before the unlock sheet covers it.
private const val UNLOCK_SHEET_DELAY_MILLIS = 600L

@Composable
fun NumericalChallengeScreen(
  challenge: NumericalChallenge,
  unlockMinutes: Int,
  onUnlockApp: () -> Unit,
  onExitChallenge: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var answer by rememberSaveable(challenge) { mutableStateOf("") }
  var answerState by remember(challenge) { mutableStateOf(AnswerState.Idle) }
  // Bumped on every wrong submission so submitting the same wrong answer shakes the card again.
  var wrongAttempts by remember(challenge) { mutableIntStateOf(0) }
  var showUnlockSheet by rememberSaveable(challenge) { mutableStateOf(false) }
  val keyboardController = LocalSoftwareKeyboardController.current

  LaunchedEffect(answerState) {
    if (answerState == AnswerState.Correct) {
      keyboardController?.hide()
      delay(UNLOCK_SHEET_DELAY_MILLIS)
      showUnlockSheet = true
    }
  }

  fun onAnswerChange(value: String) {
    answer = value
    if (answerState == AnswerState.Wrong) answerState = AnswerState.Idle
  }

  fun submitAnswer(response: Int?) {
    if (answerState == AnswerState.Correct) return
    if (response != null && challenge.checkAnswer(response)) {
      answerState = AnswerState.Correct
    } else {
      answerState = AnswerState.Wrong
      wrongAttempts++
    }
  }

  NumericalChallengeContent(
    challengeText = challenge.string(),
    answer = answer,
    answerState = answerState,
    wrongAttempts = wrongAttempts,
    onAnswerChange = ::onAnswerChange,
    onSubmitAnswer = ::submitAnswer,
    onExitChallenge = onExitChallenge,
    modifier = modifier,
  )

  if (showUnlockSheet) {
    UnlockSheet(
      unlockMinutes = unlockMinutes,
      onUnlockApp = onUnlockApp,
      onStayFocusedClick = onExitChallenge,
    )
  }
}

@Composable
internal fun NumericalChallengeContent(
  challengeText: String,
  answer: String,
  answerState: AnswerState,
  onAnswerChange: (String) -> Unit,
  onSubmitAnswer: (Int?) -> Unit,
  onExitChallenge: () -> Unit,
  modifier: Modifier = Modifier,
  wrongAttempts: Int = 0,
) {
  val entrance = rememberEntranceState()
  val shakeOffset = rememberShakeOffset(trigger = wrongAttempts)

  Scaffold(modifier = modifier) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .consumeWindowInsets(innerPadding)
        .imePadding()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = HorizontalPadding, vertical = VerticalPadding),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Spacer(Modifier.height(HeaderTopSpacing))
      ChallengeHeader(entrance)
      Spacer(Modifier.height(HeaderBottomSpacing))
      Column(
        modifier = Modifier
          .widthIn(max = ContentMaxWidth)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ContentSpacing),
      ) {
        EntranceItem(entrance, order = 2) {
          PlugCard(
            challengeText = challengeText,
            inputValue = answer,
            onInputChange = onAnswerChange,
            onCheckAnswer = onSubmitAnswer,
            placeholder = stringResource(R.string.challenge_answer_placeholder),
            ctaText = stringResource(R.string.challenge_check_answer),
            ctaIcon = Icons.Rounded.Quiz,
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

private object NumericalChallengeScreenDefaults {
  val HorizontalPadding = 32.dp
  val VerticalPadding = 24.dp
  val HeaderTopSpacing = 40.dp
  val HeaderBottomSpacing = 24.dp
  val ContentMaxWidth = 400.dp
  val ContentSpacing = 40.dp
}

@Preview(showSystemUi = true)
@Composable
private fun NumericalScreenPreview() {
  PlugBrainTheme(dynamicColor = false) {
    NumericalChallengeScreen(
      challenge = AdditionTwoDigitsCarryFreeChallenge(),
      unlockMinutes = 5,
      onUnlockApp = {},
      onExitChallenge = {},
    )
  }
}

@Preview(name = "Wrong answer", showSystemUi = true)
@Composable
private fun NumericalScreenWrongPreview() {
  NumericalContentPreview(answer = "99", answerState = AnswerState.Wrong)
}

@Preview(name = "Correct answer", showSystemUi = true)
@Composable
private fun NumericalScreenCorrectPreview() {
  NumericalContentPreview(answer = "42", answerState = AnswerState.Correct)
}

@Composable
private fun NumericalContentPreview(
  answer: String,
  answerState: AnswerState,
) {
  PlugBrainTheme(dynamicColor = false) {
    NumericalChallengeContent(
      challengeText = "25 + 17",
      answer = answer,
      answerState = answerState,
      onAnswerChange = {},
      onSubmitAnswer = {},
      onExitChallenge = {},
    )
  }
}
