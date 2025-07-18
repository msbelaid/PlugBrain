package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CountChallengeScreen(
  number: Int,
  checkAnswer: (Int) -> Unit,
) {
  var userInput by remember { mutableStateOf("") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Text(
      text = "How many objects do you see?",
      style = MaterialTheme.typography.headlineSmall,
    )

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .padding(vertical = 8.dp),
    ) {
      items(number) {
        Box(
          modifier = Modifier
            .size(16.dp)
            .background(
              color = Color.Red,
              shape = CircleShape,
            ),
        )
      }
    }

    TextField(
      value = userInput,
      onValueChange = { userInput = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
      singleLine = true,
      modifier = Modifier.fillMaxWidth(),
    )

    Button(
      onClick = { checkAnswer(userInput.toInt()) },
    ) {
      Text(">", style = MaterialTheme.typography.headlineLarge)
    }
  }
}

@Preview
@Composable
private fun CountChallengeScreenPreview() {
  CountChallengeScreen(10, {})
}
