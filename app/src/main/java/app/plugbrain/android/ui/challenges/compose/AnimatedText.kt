package app.plugbrain.android.ui.challenges.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

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
