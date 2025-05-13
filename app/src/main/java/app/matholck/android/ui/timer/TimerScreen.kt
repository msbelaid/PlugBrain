package app.matholck.android.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
  onTimeFinished: () -> Unit,
) {
  var timeLeft by remember { mutableIntStateOf(startTime) }

  LaunchedEffect(Unit) {
    while (timeLeft > 0) {
      delay(1000L)
      timeLeft--
      if (timeLeft == 0) onTimeFinished()
    }
  }

  Box(Modifier.fillMaxSize()) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.align(Alignment.Center),
    ) {
      //
      Text(
        text = "Are you sure you want to waste your precious time on something that adds nothing to your life?",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(32.dp),
      )
      Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
          onClick = { },
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
          ),
        ) {
          Text("No,\n take me out", textAlign = TextAlign.Center)
        }
        Button(
          enabled = timeLeft == 0,
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
          ),
          onClick = { },
        ) {
          Text("Yes, In understand\nwait $timeLeft", textAlign = TextAlign.Center)
        }
      }
      /*Text("Time Left:", style = MaterialTheme.typography.titleLarge)
      Text("$timeLeft", style = MaterialTheme.typography.displayLarge)
      if (timeLeft > 0) {
        CircularProgressIndicator(
          progress = { timeLeft / startTime.toFloat() },
          modifier = Modifier
            .padding(top = 24.dp)
            .size(60.dp),
          strokeWidth = 6.dp
        )
      }*/
    }
  }
}

@Preview
@Composable
private fun TimerScreenPreviewStartTime() {
  TimerScreen(60) {}
}

@Preview
@Composable
private fun TimerScreenPreviewEndTime() {
  TimerScreen(0) {}
}
