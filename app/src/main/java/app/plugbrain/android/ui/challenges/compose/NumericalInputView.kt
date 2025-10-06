package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.android.awaitFrame

@Composable
fun NumericalInputView(checkAnswer: (Int?) -> Unit) {
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
        onClick = { if (responseText.isNotEmpty()) checkAnswer(responseText.toIntOrNull()) },
      ) { Text(">", style = MaterialTheme.typography.displayLarge) }
    },
    keyboardActions = KeyboardActions(
      onDone = { checkAnswer(responseText.toIntOrNull()) },
    ),
    modifier = Modifier
      .fillMaxWidth()
      .focusRequester(focusRequester),
  )
}

@Preview
@Composable
private fun NumericalInputViewPreview() {
  NumericalInputView(checkAnswer = {})
}
