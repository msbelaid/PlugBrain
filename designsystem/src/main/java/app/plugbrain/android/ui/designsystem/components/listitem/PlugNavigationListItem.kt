package app.plugbrain.android.ui.designsystem.components.listitem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SignalCellularAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.theme.MathlockAppTheme

/**
 * A row that shows a setting's current value and opens the place to change it.
 * [supportingContent] goes under the title, in place of or after [description].
 */
@Composable
fun PlugNavigationListItem(
  title: String,
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  description: String? = null,
  supportingContent: (@Composable () -> Unit)? = null,
) {
  PlugListCard(
    isSelected = false,
    clickModifier = Modifier.clickable(role = Role.Button, onClick = onClick),
    modifier = modifier.fillMaxWidth(),
    highlightSelection = false,
  ) {
    LeadingIcon(
      icon = icon,
      isSelected = false,
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(6.dp),
      modifier = Modifier.weight(1f),
    ) {
      Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
      if (description != null) {
        Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      supportingContent?.invoke()
    }
    TrailingIcon(isSelected = false)
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
private fun PlugNavigationListItemPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      PlugNavigationListItem(
        title = "Block interval",
        description = "Every 15 minutes of use",
        icon = Icons.Rounded.Schedule,
        onClick = {},
      )
      PlugNavigationListItem(
        title = "Minimum difficulty",
        description = "Beginner · level 1",
        icon = Icons.Rounded.SignalCellularAlt,
        onClick = {},
      )
    }
  }
}
