package app.plugbrain.android.ui.challenges.compose

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R

// The app icon and title, each entering in turn before the rest of the screen.
@Composable
internal fun ChallengeHeader(
  entrance: MutableTransitionState<Boolean>,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    EntranceItem(entrance, order = 0) { AppIcon() }
    Spacer(Modifier.height(16.dp))
    EntranceItem(entrance, order = 1) {
      Text(
        text = stringResource(R.string.challenge_title),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
      )
    }
  }
}

// The launcher drawables are adaptive icon layers (108dp with a 72dp safe zone),
// so they are stacked and cropped to the visible area, like a launcher would.
@Composable
private fun AppIcon() {
  Box(
    modifier = Modifier
      .size(AppIconSize)
      .clip(RoundedCornerShape(16.dp)),
    contentAlignment = Alignment.Center,
  ) {
    Image(
      painter = painterResource(R.drawable.ic_launcher_background),
      contentDescription = null,
      modifier = Modifier.requiredSize(AppIconSize * 1.5f),
    )
    Image(
      painter = painterResource(R.drawable.ic_launcher),
      contentDescription = null,
      modifier = Modifier.requiredSize(AppIconSize * 1.5f),
    )
  }
}

private val AppIconSize = 64.dp
