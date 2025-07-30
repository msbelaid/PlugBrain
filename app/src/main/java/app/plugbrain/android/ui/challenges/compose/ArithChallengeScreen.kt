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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.repository.model.MathChallenge
import app.plugbrain.android.repository.model.Operator
import app.plugbrain.android.ui.isPortrait

@Composable
fun ArithChallengeScreen(
  modifier: Modifier = Modifier,
  mathChallenge: MathChallenge,
  isWrong: Boolean,
  checkAnswer: (Int) -> Unit,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(32.dp),
    modifier = modifier.padding(top = 32.dp),
  ) {
    BlockAppsMessage()
    if (isPortrait()) {
      OperationView(mathChallenge)
      ResponseInputView(checkAnswer)
    } else {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Spacer(Modifier.width(32.dp))
        OperationView(mathChallenge)
        Text("=", style = MaterialTheme.typography.displayLarge)
        ResponseInputView(checkAnswer)
      }
    }
    if (isWrong) ErrorMessage()
  }
}

@Composable
fun ErrorMessage() {
  // TODO create an array with error messages, select randomly
  Text("Wrong Answer", color = Color.Red)
}

// TODO a list of messages, pick randomly
@Composable
private fun BlockAppsMessage() {
  Text(
    text = "Time's up!\nConnect your brain to unlock the app!",
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
private fun OperationView(mathChallenge: MathChallenge) {
  Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
    Text(
      text = mathChallenge.num1.toString(),
      style = MaterialTheme.typography.displayLarge,
    )
    Text(text = mathChallenge.operator.symbol, style = MaterialTheme.typography.displayLarge)
    Text(text = mathChallenge.num2.toString(), style = MaterialTheme.typography.displayLarge)
  }
}

@Preview(name = "Portrait", widthDp = 360, heightDp = 640)
@Composable
private fun ArithChallengePortraitScreenPreview() {
  ArithChallengeScreen(
    mathChallenge = MathChallenge(
      num1 = 999,
      num2 = 999,
      operator = Operator.ADDITION,
    ),
    isWrong = false,
    checkAnswer = {},
  )
}

@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Preview(name = "Landscape Dark", widthDp = 640, heightDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArithChallengeLandscapeScreenPreview() {
  ArithChallengeScreen(
    mathChallenge = MathChallenge(
      num1 = 999,
      num2 = 999,
      operator = Operator.ADDITION,
    ),
    isWrong = true,
    checkAnswer = {},
  )
}
