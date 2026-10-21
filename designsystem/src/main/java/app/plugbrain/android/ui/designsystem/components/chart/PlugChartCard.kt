package app.plugbrain.android.ui.designsystem.components.chart

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.plugbrain.android.ui.theme.MathlockAppTheme

/**
 * A card that frames a chart: [title] and [subtitle] on the left, an optional [badge] with the
 * current value on the right, then [content], such as a [PlugTimelineChart] and its legend.
 */
@Composable
fun PlugChartCard(
  title: String,
  modifier: Modifier = Modifier,
  subtitle: String? = null,
  badge: String? = null,
  content: @Composable ColumnScope.() -> Unit,
) {
  val smallText = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp, lineHeight = 16.sp)
  OutlinedCard(
    modifier = modifier,
    shape = CardShape,
    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier.semantics(mergeDescendants = true) {},
      ) {
        Column(
          verticalArrangement = Arrangement.spacedBy(2.dp),
          modifier = Modifier.weight(1f),
        ) {
          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
          )
          if (subtitle != null) {
            Text(text = subtitle, style = smallText, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
        if (badge != null) {
          Text(
            text = badge,
            style = smallText.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
              .clip(BadgeShape)
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 10.dp, vertical = 4.dp),
          )
        }
      }
      content()
    }
  }
}

private val CardShape = RoundedCornerShape(16.dp)
private val BadgeShape = RoundedCornerShape(50)

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
private fun PlugChartCardPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      SampleTimelineCard()
    }
  }
}

/** A day of sample data, shared by the preview and the screenshot tests. */
@Composable
internal fun SampleTimelineCard(modifier: Modifier = Modifier) {
  val hour = 60 * 60_000L
  PlugChartCard(
    title = "Challenge difficulty",
    subtitle = "Last 24 hours",
    badge = "Level 3 now",
    modifier = modifier,
  ) {
    PlugTimelineChart(
      startTime = 0,
      endTime = 24 * hour,
      steps = listOf(
        PlugTimelineStep(0, 2),
        PlugTimelineStep(2 * hour, 3),
        PlugTimelineStep(3 * hour, 4),
        PlugTimelineStep(5 * hour, 2),
        PlugTimelineStep(9 * hour, 4),
        PlugTimelineStep(10 * hour, 7),
        PlugTimelineStep(11 * hour, 10),
        PlugTimelineStep(13 * hour, 8),
        PlugTimelineStep(15 * hour, 5),
        PlugTimelineStep(17 * hour, 1),
        PlugTimelineStep(23 * hour, 3),
      ),
      bands = listOf(
        PlugTimelineBand(0, hour, PlugTimelineTone.Neutral),
        PlugTimelineBand(hour, 4 * hour, PlugTimelineTone.Negative),
        PlugTimelineBand(4 * hour, 8 * hour, PlugTimelineTone.Positive),
        PlugTimelineBand(8 * hour, 9 * hour, PlugTimelineTone.Neutral),
        PlugTimelineBand(9 * hour, 12 * hour, PlugTimelineTone.Negative),
        PlugTimelineBand(12 * hour, 22 * hour, PlugTimelineTone.Positive),
        PlugTimelineBand(22 * hour, 23 * hour, PlugTimelineTone.Neutral),
        PlugTimelineBand(23 * hour, 24 * hour, PlugTimelineTone.Negative),
      ),
      maxValue = 12,
      valueTicks = listOf(1, 5, 10),
      timeLabels = listOf(
        PlugTimelineLabel(0, "9 AM"),
        PlugTimelineLabel(6 * hour, "3 PM"),
        PlugTimelineLabel(12 * hour, "9 PM"),
        PlugTimelineLabel(18 * hour, "3 AM"),
        PlugTimelineLabel(24 * hour, "Now"),
      ),
      contentDescription = "Challenge difficulty over the last 24 hours, level 3 now.",
    )
    PlugTimelineLegend(
      items = listOf(
        PlugTimelineTone.Positive to "Away from phone",
        PlugTimelineTone.Negative to "Distracting apps",
        PlugTimelineTone.Neutral to "Other apps",
      ),
    )
  }
}
