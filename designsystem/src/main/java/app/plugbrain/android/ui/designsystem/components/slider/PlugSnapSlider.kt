package app.plugbrain.android.ui.designsystem.components.slider

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.theme.GradientEnd
import app.plugbrain.android.ui.theme.MathlockAppTheme
import app.plugbrain.android.ui.theme.PrimaryGradient
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * A slider over every whole number from the first to the last of [snapValues]. The snap values sit
 * evenly spaced along the track, with their labels under it, and the numbers between two of them
 * share that section evenly. Near a snap value the thumb is pulled onto it, like a magnet, with a
 * haptic tick. [valueDescription] is what TalkBack reads for a value, for example "8 minutes".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlugSnapSlider(
  value: Int,
  snapValues: List<Int>,
  onValueChange: (Int) -> Unit,
  modifier: Modifier = Modifier,
  valueDescription: (Int) -> String = { it.toString() },
) {
  val min = snapValues.first()
  val max = snapValues.last()
  val haptics = LocalHapticFeedback.current
  val interactionSource = remember { MutableInteractionSource() }
  val description = valueDescription(value)
  val position = snapValues.positionOf(value)
  val lastPosition = (snapValues.size - 1).toFloat()

  fun select(newValue: Int) {
    if (newValue == value) return
    if (newValue in snapValues) haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    onValueChange(newValue)
  }

  Column(modifier = modifier.fillMaxWidth()) {
    // TalkBack gets a slider over the values themselves, one per step, instead of positions.
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clearAndSetSemantics {
          stateDescription = description
          progressBarRangeInfo = ProgressBarRangeInfo(value.toFloat(), min.toFloat()..max.toFloat(), steps = max - min - 1)
          setProgress { target ->
            val rounded = target.roundToInt().coerceIn(min, max)
            val next = when {
              rounded != value -> rounded
              target > value -> (value + 1).coerceAtMost(max)
              else -> (value - 1).coerceAtLeast(min)
            }
            select(next)
            next != value
          }
        },
    ) {
      Slider(
        value = position,
        onValueChange = { select(snapValues.valueAt(it)) },
        valueRange = 0f..lastPosition,
        interactionSource = interactionSource,
        thumb = {
          SliderDefaults.Thumb(
            interactionSource = interactionSource,
            colors = SliderDefaults.colors(thumbColor = GradientEnd),
            thumbSize = ThumbSize,
          )
        },
        track = { SnapTrack(fraction = position / lastPosition) },
      )
    }
    SnapLabels(snapValues = snapValues, selected = value)
  }
}

/** The same bar as the design system's progress bars: the primary gradient over surfaceVariant. */
@Composable
private fun SnapTrack(fraction: Float) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(TrackHeight)
      .clip(TrackShape)
      .background(MaterialTheme.colorScheme.surfaceVariant),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(fraction.coerceIn(0f, 1f))
        .fillMaxHeight()
        .clip(TrackShape)
        .background(PrimaryGradient),
    )
  }
}

/** The labels sit under the track, centred on where the thumb rests for each snap value. */
@Composable
private fun SnapLabels(snapValues: List<Int>, selected: Int) {
  Layout(
    content = {
      snapValues.forEach { snapValue ->
        val isSelected = snapValue == selected
        Text(
          text = snapValue.toString(),
          style = MaterialTheme.typography.labelMedium,
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
          color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    },
    // The slider already tells TalkBack its value, so the scale is only visual.
    modifier = Modifier
      .fillMaxWidth()
      .clearAndSetSemantics { },
  ) { measurables, constraints ->
    val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }
    val width = constraints.maxWidth
    // The thumb's centre travels between half a thumb from each edge.
    val inset = ThumbSize.width.roundToPx() / 2
    val height = placeables.maxOfOrNull { it.height } ?: 0
    layout(width, height) {
      placeables.forEachIndexed { index, placeable ->
        val fraction = index.toFloat() / (placeables.size - 1)
        val centre = inset + ((width - 2 * inset) * fraction).roundToInt()
        placeable.placeRelative((centre - placeable.width / 2).coerceIn(0, width - placeable.width), 0)
      }
    }
  }
}

// Snap value i sits at position i; values between two snap values share that section evenly.
internal fun List<Int>.positionOf(value: Int): Float {
  val clamped = value.coerceIn(first(), last())
  val section = indexOfLast { it <= clamped }.coerceAtMost(size - 2)
  return section + (clamped - this[section]).toFloat() / (this[section + 1] - this[section])
}

internal fun List<Int>.valueAt(position: Float): Int {
  val nearest = position.roundToInt().coerceIn(0, size - 1)
  if (abs(position - nearest) < MAGNET_RADIUS) return this[nearest]
  val section = position.toInt().coerceIn(0, size - 2)
  val start = this[section]
  return (start + (position - section) * (this[section + 1] - start)).roundToInt()
}

// How close to a snap value, in sections, the thumb has to be to be pulled onto it.
private const val MAGNET_RADIUS = 0.12f

private val ThumbSize = DpSize(4.dp, 44.dp)
private val TrackHeight = 8.dp
private val TrackShape = RoundedCornerShape(50)

@Preview(
  name = "Light Mode",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugSnapSliderPreview() {
  MathlockAppTheme(dynamicColor = false) {
    var value by remember { mutableIntStateOf(10) }
    PlugSnapSlider(
      value = value,
      snapValues = listOf(1, 2, 5, 10, 15, 20),
      onValueChange = { value = it },
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(24.dp),
    )
  }
}
