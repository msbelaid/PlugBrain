package app.plugbrain.android.ui.designsystem.components.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.plugbrain.android.ui.theme.Error300
import app.plugbrain.android.ui.theme.Error500
import app.plugbrain.android.ui.theme.Green300
import app.plugbrain.android.ui.theme.Green500
import app.plugbrain.android.ui.theme.Neutral300

/** How a stretch of time on a [PlugTimelineChart] reads: good, bad or neither. */
enum class PlugTimelineTone {
  Positive,
  Negative,
  Neutral,
}

/** The value becomes [value] at [time] and holds until the next step. */
data class PlugTimelineStep(
  val time: Long,
  val value: Int,
)

/** The line and the area under it take the color of [tone] from [start] to [end]. */
data class PlugTimelineBand(
  val start: Long,
  val end: Long,
  val tone: PlugTimelineTone,
)

data class PlugTimelineLabel(
  val time: Long,
  val text: String,
)

/**
 * A value over time, drawn as a step line with a soft area under it, colored by [bands].
 * Times are in milliseconds; the chart spans [startTime] to [endTime] and marks the value at
 * [endTime] with a dot. Parts covered by no band or before the first step are left empty.
 *
 * @param valueTicks values that get a dashed grid line and a label on the left.
 * @param contentDescription what the chart shows, for screen readers.
 */
@Composable
fun PlugTimelineChart(
  startTime: Long,
  endTime: Long,
  steps: List<PlugTimelineStep>,
  bands: List<PlugTimelineBand>,
  maxValue: Int,
  valueTicks: List<Int>,
  timeLabels: List<PlugTimelineLabel>,
  contentDescription: String,
  modifier: Modifier = Modifier,
) {
  val colors = timelineColors()
  val gridColor = MaterialTheme.colorScheme.outlineVariant
  val ringColor = MaterialTheme.colorScheme.surface
  val labelStyle = chartLabelStyle().copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
  val textMeasurer = rememberTextMeasurer()
  val pieces = remember(startTime, endTime, steps, bands) { timelinePieces(startTime, endTime, steps, bands) }
  val timeLabelHeight = with(LocalDensity.current) {
    textMeasurer.measure("0", labelStyle).size.height.toDp()
  }

  Canvas(
    modifier = modifier
      .fillMaxWidth()
      .height(PlotHeight + LabelGap + timeLabelHeight)
      .semantics { this.contentDescription = contentDescription },
  ) {
    val tickLabels = valueTicks.map { it to textMeasurer.measure(it.toString(), labelStyle) }
    val dotRadius = DotRadius.toPx()
    val left = (tickLabels.maxOfOrNull { it.second.size.width } ?: 0) + LabelGap.toPx()
    val right = size.width - dotRadius
    val top = dotRadius
    val bottom = PlotHeight.toPx()
    val span = (endTime - startTime).coerceAtLeast(1)
    fun x(time: Long) = left + (time - startTime).toFloat() / span * (right - left)
    fun y(value: Int) = bottom - value.toFloat() / maxValue.coerceAtLeast(1) * (bottom - top)

    val dash = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 4.dp.toPx()))
    tickLabels.forEach { (value, layout) ->
      drawLine(gridColor, Offset(left, y(value)), Offset(right, y(value)), strokeWidth = 1.dp.toPx(), pathEffect = dash)
      drawText(layout, topLeft = Offset(left - LabelGap.toPx() - layout.size.width, y(value) - layout.size.height / 2f))
    }
    drawLine(gridColor, Offset(left, bottom), Offset(right, bottom), strokeWidth = 1.dp.toPx())

    pieces.forEach { piece ->
      drawRect(
        color = colors.fill(piece.tone),
        topLeft = Offset(x(piece.start), y(piece.value)),
        size = Size(x(piece.end) - x(piece.start), bottom - y(piece.value)),
      )
    }
    val stroke = Stroke(width = LineWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    pieces.forEachIndexed { index, piece ->
      val previous = pieces.getOrNull(index - 1)?.takeIf { it.end == piece.start }
      val path = Path().apply {
        // A change of value is drawn as a vertical edge, in the color of the piece it leads into.
        moveTo(x(piece.start), y(previous?.value ?: piece.value))
        lineTo(x(piece.start), y(piece.value))
        lineTo(x(piece.end), y(piece.value))
      }
      drawPath(path, colors.line(piece.tone), style = stroke)
    }
    pieces.lastOrNull()?.takeIf { it.end == endTime }?.let { last ->
      val center = Offset(x(last.end), y(last.value))
      drawCircle(ringColor, radius = dotRadius + 2.dp.toPx(), center = center)
      drawCircle(colors.line(last.tone), radius = dotRadius, center = center)
    }

    timeLabels.forEach { label ->
      val layout = textMeasurer.measure(label.text, labelStyle)
      val labelLeft = (x(label.time) - layout.size.width / 2f).coerceIn(0f, (size.width - layout.size.width).coerceAtLeast(0f))
      drawText(layout, topLeft = Offset(labelLeft, bottom + LabelGap.toPx()))
    }
  }
}

/** The colors of a [PlugTimelineChart], each with the label the screen gives it. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlugTimelineLegend(
  items: List<Pair<PlugTimelineTone, String>>,
  modifier: Modifier = Modifier,
) {
  val colors = timelineColors()
  val labelStyle = chartLabelStyle()
  FlowRow(
    horizontalArrangement = Arrangement.spacedBy(14.dp),
    verticalArrangement = Arrangement.spacedBy(6.dp),
    modifier = modifier,
  ) {
    items.forEach { (tone, label) ->
      Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(colors.line(tone)),
        )
        Text(label, style = labelStyle, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  }
}

internal data class TimelinePiece(
  val start: Long,
  val end: Long,
  val value: Int,
  val tone: PlugTimelineTone,
)

/** Cuts the steps at band edges, so each piece has one value and one tone. */
internal fun timelinePieces(
  startTime: Long,
  endTime: Long,
  steps: List<PlugTimelineStep>,
  bands: List<PlugTimelineBand>,
): List<TimelinePiece> {
  val sortedSteps = steps.sortedBy { it.time }
  val pieces = mutableListOf<TimelinePiece>()
  for (band in bands.sortedBy { it.start }) {
    val to = minOf(band.end, endTime)
    var cursor = maxOf(band.start, startTime)
    var index = sortedSteps.indexOfLast { it.time <= cursor }
    if (index < 0) {
      // No value yet when the band starts: begin at the first step inside it.
      index = sortedSteps.indexOfFirst { it.time < to }.takeIf { it >= 0 } ?: continue
      cursor = sortedSteps[index].time
    }
    while (cursor < to && index < sortedSteps.size) {
      val next = sortedSteps.getOrNull(index + 1)?.time?.coerceAtMost(to) ?: to
      if (next > cursor) pieces += TimelinePiece(cursor, next, sortedSteps[index].value, band.tone)
      cursor = maxOf(cursor, next)
      index++
    }
  }
  return pieces
}

private data class TimelineColors(
  val positive: Color,
  val negative: Color,
  val neutral: Color,
) {
  fun line(tone: PlugTimelineTone) = when (tone) {
    PlugTimelineTone.Positive -> positive
    PlugTimelineTone.Negative -> negative
    PlugTimelineTone.Neutral -> neutral
  }

  fun fill(tone: PlugTimelineTone) = line(tone).copy(alpha = if (tone == PlugTimelineTone.Neutral) 0.24f else 0.16f)
}

@Composable
private fun timelineColors(): TimelineColors {
  // Like PlugStatusCard: these shades aren't in the color scheme, so pick them by the surface.
  val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
  return TimelineColors(
    positive = if (isDark) Green300 else Green500,
    negative = if (isDark) Error300 else Error500,
    neutral = Neutral300,
  )
}

@Composable
private fun chartLabelStyle(): TextStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp, lineHeight = 16.sp)

private val PlotHeight = 120.dp
private val LabelGap = 8.dp
private val DotRadius = 4.dp
private val LineWidth = 2.dp
