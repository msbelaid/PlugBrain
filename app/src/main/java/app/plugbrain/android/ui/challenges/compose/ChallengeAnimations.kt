package app.plugbrain.android.ui.challenges.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp

// Previews and screenshot tests render a single frame, so start fully visible there.
@Composable
internal fun rememberEntranceState(): MutableTransitionState<Boolean> {
  val inspectionMode = LocalInspectionMode.current
  val state = remember { MutableTransitionState(inspectionMode) }
  LaunchedEffect(Unit) { state.targetState = true }
  return state
}

// Fades and lifts each block in one after the other, to set a calmer pace.
@Composable
internal fun EntranceItem(
  state: MutableTransitionState<Boolean>,
  order: Int,
  content: @Composable () -> Unit,
) {
  val delayMillis = order * ENTRANCE_STAGGER_MILLIS
  AnimatedVisibility(
    visibleState = state,
    enter = fadeIn(tween(ENTRANCE_DURATION_MILLIS, delayMillis)) +
      slideInVertically(tween(ENTRANCE_DURATION_MILLIS, delayMillis)) { it / 4 },
  ) {
    content()
  }
}

// Shakes horizontally each time trigger changes; the initial value 0 doesn't shake.
// Apply the returned value as a translationX in a graphicsLayer.
@Composable
internal fun rememberShakeOffset(trigger: Int): Animatable<Float, AnimationVector1D> {
  val offset = remember { Animatable(0f) }
  val distance = with(LocalDensity.current) { SHAKE_DISTANCE.toPx() }
  LaunchedEffect(trigger) {
    if (trigger == 0) return@LaunchedEffect
    offset.animateTo(
      targetValue = 0f,
      animationSpec = keyframes {
        durationMillis = SHAKE_DURATION_MILLIS
        -distance at 60
        distance at 120
        -distance * 0.6f at 180
        distance * 0.6f at 240
        -distance * 0.3f at 300
      },
    )
  }
  return offset
}

private const val ENTRANCE_DURATION_MILLIS = 500
private const val ENTRANCE_STAGGER_MILLIS = 120
private const val SHAKE_DURATION_MILLIS = 360
private val SHAKE_DISTANCE = 12.dp
