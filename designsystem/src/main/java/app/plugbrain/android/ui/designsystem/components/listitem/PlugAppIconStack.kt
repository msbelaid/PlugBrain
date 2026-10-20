package app.plugbrain.android.ui.designsystem.components.listitem

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.theme.MathlockAppTheme

/**
 * Overlapping app icons, at most [maxVisible] of them, then "+N" for the rest. It is decorative:
 * the text next to it should already say how many apps there are.
 */
@Composable
fun PlugAppIconStack(
  icons: List<Painter>,
  modifier: Modifier = Modifier,
  maxVisible: Int = 5,
) {
  val ringColor = MaterialTheme.colorScheme.surface
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.clearAndSetSemantics {},
  ) {
    // Each icon starts a little before the previous one ends, so they overlap.
    Box {
      icons.take(maxVisible).forEachIndexed { index, icon ->
        Image(
          painter = icon,
          contentDescription = null,
          modifier = Modifier
            .padding(start = (IconSize - IconOverlap) * index)
            .size(IconSize)
            .background(color = ringColor, shape = IconShape)
            .border(width = 2.dp, color = ringColor, shape = IconShape)
            .padding(2.dp)
            .clip(IconShape),
        )
      }
    }
    val hidden = icons.size - maxVisible
    if (hidden > 0) {
      Text(
        text = "+$hidden",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 8.dp),
      )
    }
  }
}

private val IconSize = 28.dp
private val IconOverlap = 6.dp
private val IconShape = RoundedCornerShape(8.dp)

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
private fun PlugAppIconStackPreview() {
  MathlockAppTheme(dynamicColor = false) {
    val icon = rememberVectorPainter(Icons.Rounded.Android)
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(16.dp),
    ) {
      PlugAppIconStack(icons = List(3) { icon })
      PlugAppIconStack(icons = List(7) { icon })
    }
  }
}
