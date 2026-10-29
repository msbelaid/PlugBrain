package app.plugbrain.android.ui.designsystem.components.listitem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import app.plugbrain.android.ui.theme.PlugBrainTheme

/**
 * One choice in a single-choice list, such as a block interval or a difficulty.
 * Put the cards in a parent with [selectableGroup] so screen readers announce them as one group.
 */
@Composable
fun PlugSelectableCard(
  title: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  description: String? = null,
) {
  PlugListCard(
    isSelected = isSelected,
    clickModifier = Modifier.selectable(
      selected = isSelected,
      role = Role.RadioButton,
      onClick = onClick,
    ),
    modifier = modifier,
  ) {
    LeadingIcon(
      icon = icon,
      isSelected = isSelected,
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.weight(1f),
    ) {
      Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
      if (description != null) {
        Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
      }
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
private fun PlugSelectableCardPreview() {
  PlugBrainTheme(dynamicColor = false) {
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(24.dp)
        .selectableGroup(),
    ) {
      PlugSelectableCard(
        title = "Every 5 minutes",
        icon = Icons.Rounded.Schedule,
        isSelected = true,
        onClick = {},
      )
      PlugSelectableCard(
        title = "Every 15 minutes",
        icon = Icons.Rounded.Schedule,
        isSelected = false,
        onClick = {},
      )
      PlugSelectableCard(
        title = "Beginner",
        description = "Simple additions and subtractions",
        icon = Icons.Rounded.SignalCellularAlt,
        isSelected = false,
        onClick = {},
      )
    }
  }
}
