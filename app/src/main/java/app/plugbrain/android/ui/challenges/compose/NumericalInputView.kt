package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R

@Composable
fun NumericalInputView(
  modifier: Modifier = Modifier,
  checkAnswer: (Int?) -> Unit,
) {
  var responseText by remember { mutableStateOf("") }

  TextField(
    value = responseText,
    onValueChange = { responseText = it },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    textStyle = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center),
    singleLine = true,
    placeholder = {
      Text(
        text = stringResource(R.string.challenge_answer_hint),
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
      )
    },
    trailingIcon = {
      OutlinedButton(
        modifier = Modifier
          .size(70.dp)
          .clip(CircleShape)
          .testTag("Submit Answer"),
        enabled = responseText.isNotEmpty(),
        onClick = { if (responseText.isNotEmpty()) checkAnswer(responseText.toIntOrNull()) },
      ) {
        Icon(
          Icons.AutoMirrored.Rounded.KeyboardArrowRight,
          contentDescription = "Submit Answer",
        )
      }
    },
    keyboardActions = KeyboardActions(
      onDone = { checkAnswer(responseText.toIntOrNull()) },
    ),
    modifier = modifier,
    colors = TextFieldDefaults.colors( // <<< Transparent background for better placeholder visibility
      focusedContainerColor = Color.Transparent,
      unfocusedContainerColor = Color.Transparent,
      disabledContainerColor = Color.Transparent,
      focusedIndicatorColor = MaterialTheme.colorScheme.primary,
      unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    ),
  )
}

@Preview
@Composable
private fun NumericalInputViewPreview() {
  NumericalInputView(checkAnswer = {})
}
