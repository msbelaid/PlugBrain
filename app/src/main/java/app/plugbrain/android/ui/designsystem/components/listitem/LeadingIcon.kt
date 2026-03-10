package app.plugbrain.android.ui.designsystem.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
internal fun LeadingIcon(
  modifier: Modifier = Modifier.Companion,
  icon: ImageVector,
  isSelected: Boolean,
) {
  val tint =
    if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
  val background =
    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
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
