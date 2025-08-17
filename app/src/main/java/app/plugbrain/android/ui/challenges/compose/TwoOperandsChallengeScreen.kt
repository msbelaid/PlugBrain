package app.plugbrain.android.ui.challenges.compose

import android.content.res.Configuration
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.challenges.TwoOperandsChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsCarryFreeChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationFourByFourDigitChallenge
import app.plugbrain.android.ui.isPortrait

@Composable
fun TwoOperandsChallengeScreen(
  modifier: Modifier = Modifier,
  challenge: TwoOperandsChallenge,
  checkAnswer: (Int) -> Unit,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(32.dp),
    modifier = modifier.padding(top = 32.dp),
  ) {
    BlockAppsMessage()
    if (isPortrait()) {
      OperationView(challenge)
      ResponseInputView(checkAnswer)
    } else {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Spacer(Modifier.width(32.dp))
        OperationView(challenge)
        Text("=", style = MaterialTheme.typography.displayLarge)
        ResponseInputView(checkAnswer)
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
  )
}

@Composable
private fun ResponseInputView(checkAnswer: (Int) -> Unit) {
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current
  var responseText by remember { mutableStateOf("") }

  LaunchedEffect(Unit) {
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
        onClick = { if (responseText.isNotEmpty()) checkAnswer(responseText.toString().toInt()) },
      ) { Text(">", style = MaterialTheme.typography.displayLarge) }
    },
    keyboardActions = KeyboardActions(
      onDone = { checkAnswer(responseText.toString().toInt()) },
    ),
    modifier = Modifier
      .fillMaxWidth()
      .focusRequester(focusRequester)
      .focusable(),
  )
}

@Composable
private fun OperationView(challenge: TwoOperandsChallenge) {
  Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
    Text(
      text = challenge.operand1.toString(),
      style = MaterialTheme.typography.displayLarge,
    )
    Text(
      text = challenge.operationType.symbol,
      style = MaterialTheme.typography.displayLarge,
    )
    Text(
      text = challenge.operand2.toString(),
      style = MaterialTheme.typography.displayLarge,
    )
  }
}

@Preview(name = "Portrait", widthDp = 360, heightDp = 640)
@Composable
private fun ArithChallengePortraitScreenPreview() {
  TwoOperandsChallengeScreen(
    challenge = MultiplicationFourByFourDigitChallenge(),
    checkAnswer = {},
  )
}

@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Preview(name = "Landscape Dark", widthDp = 640, heightDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TwoOperandsLandscapeScreenPreview() {
  TwoOperandsChallengeScreen(
    challenge = AdditionTwoDigitsCarryFreeChallenge(),
    checkAnswer = {},
  )
}
