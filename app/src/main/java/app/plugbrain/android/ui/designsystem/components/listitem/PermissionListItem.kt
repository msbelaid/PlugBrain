package app.plugbrain.android.ui.designsystem.components.listitem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.theme.MathlockAppTheme

@Composable
fun PermissionListItem(
  title: String,
  description: String,
  icon: ImageVector,
  isGranted: Boolean,
  onClick: () -> Unit,
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .alpha(if (isGranted) 0.5f else 1f)
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline,
        shape = RoundedCornerShape(8.dp),
      )
      .background(MaterialTheme.colorScheme.surface)
      .background(
        color = if (isGranted) MaterialTheme.colorScheme.primary.copy(0.1f) else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
      )
      .clickable(onClick = onClick)
      .padding(vertical = 12.dp, horizontal = 16.dp),
  ) {
    LeadingIcon(
      icon = icon,
      isSelected = isGranted,
      modifier = Modifier.align(Alignment.Top).padding(top = 3.dp),
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.weight(1f),
    ) {
      Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
      Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
    TrailingIcon(isGranted)
  }
}

@Composable
private fun LeadingIcon(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  isSelected: Boolean,
) {
  val tint =
    if (isSelected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
  val background =
    if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant
  Icon(
    imageVector = icon,
    contentDescription = "",
    tint = tint,
    modifier = modifier
      .background(
        color = background,
        shape = RoundedCornerShape(4.dp),
      )
      .size(40.dp)
      .padding(8.dp),
  )
}

@Composable
private fun TrailingIcon(isSelected: Boolean) {
  val tint =
    if (isSelected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
  val background =
    if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent
  Icon(
    imageVector = if (isSelected) Icons.Rounded.Check else Icons.AutoMirrored.Rounded.KeyboardArrowRight,
    contentDescription = "",
    tint = tint,
    modifier = Modifier
      .background(color = background, shape = RoundedCornerShape(50))
      .size(24.dp)
      .then(if (isSelected) Modifier.padding(4.dp) else Modifier),
  )
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
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(32.dp),
    ) {
      PermissionListItem(
        title = "App Usage access",
        description = "Lets PlugBrain track app usage so it can block distracting apps at the right time.",
        isGranted = true,
        icon = Icons.Filled.Search,
        onClick = {},
      )
      PermissionListItem(
        title = "Display over other apps",
        description = "Allows PlugBrain to display a challenge while you use a distracting app.",
        isGranted = false,
        icon = Icons.Rounded.Layers,
        onClick = {},
      )
    }
  }
}
