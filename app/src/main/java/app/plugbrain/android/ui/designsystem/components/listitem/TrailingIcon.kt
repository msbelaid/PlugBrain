package app.plugbrain.android.ui.designsystem.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun TrailingIcon(isSelected: Boolean) {
  val tint =
    if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
  val background =
    if (isSelected) MaterialTheme.colorScheme.primary else Color.Companion.Transparent
  Icon(
    imageVector = if (isSelected) Icons.Rounded.Check else Icons.AutoMirrored.Rounded.KeyboardArrowRight,
    contentDescription = "",
    tint = tint,
    modifier = Modifier.Companion
      .background(color = background, shape = RoundedCornerShape(50))
      .size(24.dp)
      .then(if (isSelected) Modifier.Companion.padding(4.dp) else Modifier.Companion),
  )
}
