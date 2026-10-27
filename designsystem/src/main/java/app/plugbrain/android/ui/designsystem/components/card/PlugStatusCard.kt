package app.plugbrain.android.ui.designsystem.components.card

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonPrimary
import app.plugbrain.android.ui.theme.MathlockAppTheme
import app.plugbrain.android.ui.theme.Primary300
import app.plugbrain.android.ui.theme.Primary600
import app.plugbrain.android.ui.theme.PrimaryGradient
import app.plugbrain.android.ui.theme.Warning100
import app.plugbrain.android.ui.theme.Warning300
import app.plugbrain.android.ui.theme.Warning400
import app.plugbrain.android.ui.theme.Warning700
import app.plugbrain.android.ui.theme.Warning900

/**
 * Tells the user what PlugBrain is doing right now. Only [label] and [icon] are required;
 * each other slot shows when it is set, in this order: headline, progress, body, action.
 *
 * @param headline a large value, such as "11 min", followed by [headlineSupportingText].
 * @param progress a fraction between 0 and 1, drawn as a bar under the headline.
 */
@Composable
fun PlugStatusCard(
  label: String,
  icon: ImageVector,
  modifier: Modifier = Modifier,
  variant: PlugStatusCardVariant = PlugStatusCardVariant.Info,
  headline: String? = null,
  headlineSupportingText: String? = null,
  body: String? = null,
  progress: Float? = null,
  actionText: String? = null,
  onAction: () -> Unit = {},
) {
  val colors = statusCardColors(variant)
  OutlinedCard(
    modifier = modifier
      .then(
        if (variant == PlugStatusCardVariant.Info) {
          Modifier.shadow(elevation = CardElevation, shape = CardShape, clip = false)
        } else {
          Modifier
        },
      ),
    shape = CardShape,
    colors = CardDefaults.outlinedCardColors(containerColor = colors.container),
    border = BorderStroke(width = 1.dp, color = colors.border),
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(14.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
    ) {
      // The texts read as one group; the action button stays its own focus stop.
      Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.semantics(mergeDescendants = true) {},
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.accent,
            modifier = Modifier.size(20.dp),
          )
          Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = colors.accent,
            modifier = Modifier.semantics { heading() },
          )
        }
        if (headline != null) {
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              text = headline,
              style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.alignByBaseline(),
            )
            if (headlineSupportingText != null) {
              Text(
                text = headlineSupportingText,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.secondaryText,
                modifier = Modifier
                  .alignByBaseline()
                  .weight(1f, fill = false),
              )
            }
          }
        }
        if (progress != null) {
          StatusProgressBar(progress)
        }
        if (body != null) {
          Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText,
          )
        }
      }
      if (actionText != null) {
        PlugButtonPrimary(
          text = actionText,
          onClick = onAction,
          modifier = Modifier.fillMaxWidth(),
        )
      }
    }
  }
}

@Composable
private fun StatusProgressBar(progress: Float) {
  val fraction = progress.coerceIn(0f, 1f)
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(8.dp)
      .semantics { progressBarRangeInfo = ProgressBarRangeInfo(current = fraction, range = 0f..1f) }
      .clip(BarShape)
      .background(MaterialTheme.colorScheme.surfaceVariant),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(fraction)
        .fillMaxHeight()
        .clip(BarShape)
        .background(PrimaryGradient),
    )
  }
}

private data class StatusCardColors(
  val container: Color,
  val border: Color,
  val accent: Color,
  val secondaryText: Color,
)

@Composable
private fun statusCardColors(variant: PlugStatusCardVariant): StatusCardColors {
  val scheme = MaterialTheme.colorScheme
  // The accent shades aren't in the color scheme, so pick them by how dark the surface is.
  val isDark = scheme.surface.luminance() < 0.5f
  return when (variant) {
    PlugStatusCardVariant.Info -> StatusCardColors(
      container = scheme.surface,
      border = scheme.primary.copy(alpha = 0.3f),
      accent = if (isDark) Primary300 else Primary600,
      secondaryText = scheme.onSurfaceVariant,
    )
    PlugStatusCardVariant.Warning -> StatusCardColors(
      container = if (isDark) Warning900 else Warning100,
      border = if (isDark) Warning700 else Warning400,
      accent = if (isDark) Warning300 else Warning700,
      secondaryText = scheme.onSurface,
    )
  }
}

private val CardShape = RoundedCornerShape(16.dp)
private val CardElevation = 4.dp
private val BarShape = RoundedCornerShape(50)

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
private fun PlugStatusCardProtectionOnPreview() {
  StatusCardPreviewContainer {
    PlugStatusCard(
      label = "Protection on",
      icon = Icons.Rounded.VerifiedUser,
      headline = "11 min",
      headlineSupportingText = "left before the next challenge",
      progress = 4f / 15f,
      body = "Next challenge: level 4 of 15",
    )
  }
}

@Preview(
  name = "Light Mode - Apps Locked",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode - Apps Locked",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugStatusCardAppsLockedPreview() {
  StatusCardPreviewContainer {
    PlugStatusCard(
      label = "Apps locked",
      icon = Icons.Rounded.Lock,
      headline = "Away 6 min",
      body = "Solve a level 5 challenge to unlock. The longer you stay away, the easier it gets.",
    )
  }
}

@Preview(
  name = "Light Mode - Protection Paused",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode - Protection Paused",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugStatusCardProtectionPausedPreview() {
  StatusCardPreviewContainer {
    PlugStatusCard(
      label = "Protection paused",
      icon = Icons.Rounded.WarningAmber,
      variant = PlugStatusCardVariant.Warning,
      body = "Accessibility access was turned off, so PlugBrain can't block your apps.",
      actionText = "Turn it back on",
    )
  }
}

@Preview(
  name = "Light Mode - Nothing To Block",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode - Nothing To Block",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugStatusCardNothingToBlockPreview() {
  StatusCardPreviewContainer {
    PlugStatusCard(
      label = "Nothing to block yet",
      icon = Icons.Rounded.Apps,
      body = "Pick the apps that pull you in, and PlugBrain will ask for a challenge after 15 minutes on them.",
      actionText = "Choose apps",
    )
  }
}

@Composable
private fun StatusCardPreviewContainer(content: @Composable () -> Unit) {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      content()
    }
  }
}
