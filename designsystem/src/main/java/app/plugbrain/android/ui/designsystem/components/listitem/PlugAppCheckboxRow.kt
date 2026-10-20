package app.plugbrain.android.ui.designsystem.components.listitem

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.theme.MathlockAppTheme

/**
 * An installed app the user can pick. The whole row toggles, and [supportingContent] goes under
 * the name (for example a usage bar). Pass a [contentPadding] without horizontal space to line the
 * row up with content that is already inset.
 */
@Composable
fun PlugAppCheckboxRow(
  name: String,
  icon: Painter,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
  supportingContent: (@Composable () -> Unit)? = null,
  contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .fillMaxWidth()
      .toggleable(
        value = isChecked,
        role = Role.Checkbox,
        onValueChange = onCheckedChange,
      )
      .padding(contentPadding),
  ) {
    // The name is read out with the row, so the icon is decorative.
    Image(
      painter = icon,
      contentDescription = null,
      modifier = Modifier.size(40.dp),
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.weight(1f),
    ) {
      Text(name, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
      if (supportingContent != null) supportingContent()
    }
    Checkbox(
      checked = isChecked,
      onCheckedChange = null,
      colors = CheckboxDefaults.colors(
        checkedColor = MaterialTheme.colorScheme.primary,
        uncheckedColor = MaterialTheme.colorScheme.outline,
        checkmarkColor = MaterialTheme.colorScheme.onPrimary,
      ),
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
private fun PlugAppCheckboxRowPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
      PlugAppCheckboxRow(
        name = "Subway Surfer",
        icon = rememberVectorPainter(Icons.Rounded.Android),
        isChecked = true,
        onCheckedChange = {},
      )
      PlugAppCheckboxRow(
        name = "Instagram",
        icon = rememberVectorPainter(Icons.Rounded.Android),
        isChecked = false,
        onCheckedChange = {},
        supportingContent = {
          Text("2h 15m", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
      )
    }
  }
}
