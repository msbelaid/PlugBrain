package app.plugbrain.android.ui.designsystem.components.card

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.theme.PlugBrainTheme

private val IllustrationShape = RoundedCornerShape(12.dp)

/** A framed illustration with a caption under it, such as one step of a "how to" guide. */
@Composable
fun PlugIllustrationCard(
  caption: String,
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier.fillMaxWidth(),
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = IllustrationShape)
        .background(color = MaterialTheme.colorScheme.surface, shape = IllustrationShape)
        .padding(16.dp),
      content = content,
    )
    Text(
      text = caption,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
    )
  }
}

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
private fun PlugIllustrationCardPreview() {
  PlugBrainTheme(dynamicColor = false) {
    Box(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(24.dp),
    ) {
      PlugIllustrationCard(caption = "First, find and select PlugBrain on the list of apps") {
        repeat(3) {
          Box(
            modifier = Modifier
              .width(160.dp)
              .height(12.dp)
              .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)),
          )
        }
      }
    }
  }
}
