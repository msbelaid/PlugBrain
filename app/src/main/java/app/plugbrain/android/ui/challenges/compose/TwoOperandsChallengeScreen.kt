package app.plugbrain.android.ui.challenges.compose

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.BuildConfig
import app.plugbrain.android.R
import app.plugbrain.android.challenges.TwoOperandsChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsCarryFreeChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationFourByFourDigitChallenge
import app.plugbrain.android.ui.isPortrait
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay

@Composable
fun TwoOperandsChallengeScreen(
  modifier: Modifier = Modifier,
  challenge: TwoOperandsChallenge,
  triggerAnimation: Boolean = true,
  checkAnswer: (Int) -> Unit,
) {
  Scaffold {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(32.dp),
      modifier = modifier.padding(it),
    ) {
      if (BuildConfig.DEBUG) {
        Text(
          text = challenge.difficultyLevel.toString(),
          style = MaterialTheme.typography.titleSmall,
        )
      }
      Image(
        modifier = Modifier
          .padding(top = 24.dp)
          .size(96.dp),
        painter = rememberAsyncImagePainter(R.mipmap.ic_launcher),
        contentDescription = "",
      )
      BlockAppsMessage()
      if (isPortrait()) {
        OperationView(challenge, triggerAnimation)
        ResponseInputView(checkAnswer)
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          Spacer(Modifier.width(32.dp))
          OperationView(challenge, triggerAnimation)
          Text("=", style = MaterialTheme.typography.displayLarge)
          ResponseInputView(checkAnswer)
        }
      }
    }
  }
}

// TODO a list of messages, pick randomly
@Composable
private fun BlockAppsMessage() {
  Text(
    text = stringResource(R.string.blocking_message),
    style = MaterialTheme.typography.titleLarge,
    textAlign = TextAlign.Center,
    modifier = Modifier.padding(horizontal = 64.dp),
  )
}

@Composable
private fun ResponseInputView(checkAnswer: (Int) -> Unit) {
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current
  var responseText by remember { mutableStateOf("") }

  LaunchedEffect(Unit) {
    awaitFrame()
    focusRequester.requestFocus()
    keyboardController?.show()
  }

  TextField(
    value = responseText,
    onValueChange = { responseText = it },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    textStyle = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center),
    singleLine = true,
    trailingIcon = {
      Button(
        onClick = { if (responseText.isNotEmpty()) checkAnswer(responseText.toInt()) },
      ) { Text(">", style = MaterialTheme.typography.displayLarge) }
    },
    keyboardActions = KeyboardActions(
      onDone = { checkAnswer(responseText.toInt()) },
    ),
    modifier = Modifier
      .fillMaxWidth()
      .focusRequester(focusRequester),
  )
}

@Composable
private fun OperationView(challenge: TwoOperandsChallenge, triggerAnimation: Boolean = true) {
  Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
    AnimatedText(
      text = challenge.operand1.toString(),
      style = MaterialTheme.typography.displayLarge,
      delayMillis = 0,
      triggerAnimation = triggerAnimation,
    )
    AnimatedText(
      text = challenge.operationType.symbol,
      style = MaterialTheme.typography.displayLarge,
      delayMillis = 150,
      triggerAnimation = triggerAnimation,
    )
    AnimatedText(
      text = challenge.operand2.toString(),
      style = MaterialTheme.typography.displayLarge,
      delayMillis = 300,
      triggerAnimation = triggerAnimation,
    )
  }
}

@Composable
fun AnimatedText(
  text: String,
  style: TextStyle,
  modifier: Modifier = Modifier,
  delayMillis: Long = 0,
  triggerAnimation: Boolean = true,
  enter: EnterTransition = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
  exit: ExitTransition = fadeOut(),
) {
  var visible by remember { mutableStateOf(triggerAnimation.not()) }

  LaunchedEffect(text) {
    delay(delayMillis)
    visible = true
  }

  AnimatedVisibility(
    visible = visible,
    enter = enter,
    exit = exit,
  ) {
    Text(
      text = text,
      style = style,
      modifier = modifier,
    )
  }
}

@Preview(name = "Portrait", widthDp = 360, heightDp = 640)
@Composable
private fun ArithChallengePortraitScreenPreview() {
  TwoOperandsChallengeScreen(
    challenge = MultiplicationFourByFourDigitChallenge(),
    checkAnswer = {},
    triggerAnimation = false,
  )
}

@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Preview(
  name = "Landscape Dark",
  widthDp = 640,
  heightDp = 360,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun TwoOperandsLandscapeScreenPreview() {
  TwoOperandsChallengeScreen(
    challenge = AdditionTwoDigitsCarryFreeChallenge(),
    checkAnswer = {},
    triggerAnimation = false,
  )
}
