package app.plugbrain.android.ui.designsystem.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

private val CardShape = RoundedCornerShape(8.dp)

/**
 * Shared frame of the bordered cards (permissions, options): a selected card gets a tint, plus a
 * primary border when [highlightSelection] is set. [clickModifier] goes inside the clip so the ripple
 * follows the rounded corners.
 */
@Composable
internal fun PlugListCard(
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  clickModifier: Modifier = Modifier,
  highlightSelection: Boolean = true,
  content: @Composable RowScope.() -> Unit,
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .clip(CardShape)
      .border(
        width = 1.dp,
        color = if (isSelected && highlightSelection) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        shape = CardShape,
      )
      .background(MaterialTheme.colorScheme.surface)
      .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface)
      .then(clickModifier)
      .padding(vertical = 12.dp, horizontal = 16.dp),
    content = content,
  )
}
