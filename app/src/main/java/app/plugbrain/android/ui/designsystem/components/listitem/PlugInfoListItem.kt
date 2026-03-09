package app.plugbrain.android.ui.designsystem.components.listitem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ShowChart
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.CenterFocusWeak
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.ShowChart
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.theme.MathlockAppTheme

@Composable
fun PlugInfoListItem(
  title: String,
  description: String,
  icon: ImageVector,
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(24.dp),
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .background(MaterialTheme.colorScheme.surface)
  ) {
    LeadingIcon(
      icon = icon,
      isSelected = false,
      modifier = Modifier.align(Alignment.Top).padding(top = 3.dp),
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.weight(1f),
    ) {
      Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
      Text(description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
    }
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
private fun PermissionListItemPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      verticalArrangement = Arrangement.spacedBy(24.dp),
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(32.dp),
    ) {
      PlugInfoListItem(
        title = "Focus your attention",
        description = "Spend less time on apps that distract you.",
        icon = Icons.Rounded.TrackChanges,
      )
      PlugInfoListItem(
        title = "Unlock with a challenge",
        description = "Solve a quick math challenge to keep using the app.",
        icon = Icons.Rounded.LockOpen,
      )
      PlugInfoListItem(
        title = "Gets harder the longer you stay",
        description = "Challenges increase in difficulty the longer you keep scrolling.",
        icon = Icons.AutoMirrored.Rounded.TrendingUp,
      )
    }
  }
}
