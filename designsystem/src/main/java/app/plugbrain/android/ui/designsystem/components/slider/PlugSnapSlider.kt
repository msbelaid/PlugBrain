package app.plugbrain.android.ui.designsystem.components.slider

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import app.plugbrain.android.ui.theme.MathlockAppTheme
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * A slider over the range of [snapValues] whose thumb only rests on those values: while dragging,
 * it jumps to the nearest one, like a magnet, with a light haptic tick. The values are labelled
 * under the track at their place on the scale, so uneven steps (1, 2, 3, 5, 8…) read as such.
 * [valueDescription] is what TalkBack reads for a value, for example "8 minutes".
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
  val colors = SliderDefaults.colors(
    thumbColor = MaterialTheme.colorScheme.primary,
    activeTrackColor = MaterialTheme.colorScheme.primary,
    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
  )
  val description = valueDescription(value)

  Column(modifier = modifier.fillMaxWidth()) {
    // The slider's own accessibility steps are too small to reach the next snap value, so
    // TalkBack gets a slider that moves one snap value at a time instead.
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clearAndSetSemantics {
          stateDescription = description
          progressBarRangeInfo = ProgressBarRangeInfo(value.toFloat(), min.toFloat()..max.toFloat())
          setProgress { target ->
            val next = snapValues.nearestTo(target).takeIf { it != value }
              ?: if (target > value) snapValues.firstOrNull { it > value } else snapValues.lastOrNull { it < value }
            next?.let(onValueChange)
            next != null
          }
        },
    ) {
      Slider(
        value = value.toFloat(),
        onValueChange = { raw ->
          val snapped = snapValues.nearestTo(raw)
          if (snapped != value) {
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onValueChange(snapped)
          }
        },
        valueRange = min.toFloat()..max.toFloat(),
        colors = colors,
        interactionSource = interactionSource,
        thumb = {
          SliderDefaults.Thumb(
            interactionSource = interactionSource,
            colors = colors,
            thumbSize = ThumbSize,
          )
        },
      )
    }
    SnapLabels(snapValues = snapValues, selected = value)
  }
}

/** The labels sit under the track, centred on where the thumb rests for each value. */
@Composable
private fun SnapLabels(snapValues: List<Int>, selected: Int) {
  val min = snapValues.first()
  val max = snapValues.last()
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
        val fraction = (snapValues[index] - min).toFloat() / (max - min)
        val centre = inset + ((width - 2 * inset) * fraction).roundToInt()
        placeable.placeRelative((centre - placeable.width / 2).coerceIn(0, width - placeable.width), 0)
      }
    }
  }
}

private fun List<Int>.nearestTo(raw: Float): Int = minBy { abs(it - raw) }

private val ThumbSize = DpSize(4.dp, 44.dp)

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
    var value by remember { mutableIntStateOf(8) }
    PlugSnapSlider(
      value = value,
      snapValues = listOf(1, 2, 3, 5, 8, 13, 21),
      onValueChange = { value = it },
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(24.dp),
    )
  }
}
