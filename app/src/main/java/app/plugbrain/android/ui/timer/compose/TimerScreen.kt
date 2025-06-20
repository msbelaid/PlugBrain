package app.plugbrain.android.ui.timer.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TimerScreen(
  startTime: Int,
  onYesClicked: () -> Unit,
  onNoClicked: () -> Unit,
) {
  var timeLeft by remember { mutableIntStateOf(startTime) }

  LaunchedEffect(Unit) {
    while (timeLeft > 0) {
      delay(1000L)
      timeLeft--
    }
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding(),
  ) {
    Box(Modifier.fillMaxSize()) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .align(Alignment.Center)
          .padding(it),
      ) {
        // TODO a random message
        Text(
          text = "Are you sure you want to waste your precious time on something that adds nothing to your life?",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.titleLarge,
          modifier = Modifier.padding(32.dp),
        )
        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Button(
            onClick = { onNoClicked() },
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
          ) {
            Text("No!\nTake me out", textAlign = TextAlign.Center)
          }
          Button(
            enabled = timeLeft == 0,
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.error,
              contentColor = MaterialTheme.colorScheme.onError,
            ),
            onClick = { onYesClicked() },
          ) {
            Text(
              if (timeLeft == 0) "Yes, I understand" else "Wait ${timeLeft}s",
              textAlign = TextAlign.Center,
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun TimerScreenPreviewStartTime() {
  TimerScreen(
    60,
    onYesClicked = { },
    onNoClicked = {},
  )
}

@Preview
@Composable
private fun TimerScreenPreviewEndTime() {
  TimerScreen(
    0,
    onYesClicked = { },
    onNoClicked = {},
  )
}
