package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.isPortrait
import coil3.compose.rememberAsyncImagePainter

@Composable
fun ChallengeHeader(modifier: Modifier = Modifier) {
  if (isPortrait()) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = modifier,
    ) { Content() }
  } else {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = modifier,
    ) { Content() }
  }
}

@Composable
private fun Content() {
  Image(
    modifier = Modifier.padding(horizontal = 32.dp).size(96.dp),
    painter = rememberAsyncImagePainter(R.mipmap.ic_launcher),
    contentDescription = "",
  )
  BlockAppsMessage()
}

// TODO a list of messages, pick randomly
@Composable
private fun BlockAppsMessage() {
  Text(
    text = stringResource(R.string.blocking_message),
    style = MaterialTheme.typography.titleLarge,
    textAlign = TextAlign.Center,
    modifier = Modifier.padding(horizontal = 32.dp),
  )
}

@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Preview
@Composable
private fun ChallengeHeaderLandscapePreview() {
  ChallengeHeader()
}
