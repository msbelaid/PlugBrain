package app.plugbrain.android.ui.designsystem.components.textfield

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.theme.PlugBrainTheme

private val FieldShape = RoundedCornerShape(8.dp)
private val IconSize = 24.dp

/**
 * Single-line search box. Once there is a query, the search icon becomes a clear button, read out
 * with [clearContentDescription]. The keyboard's search key hides the keyboard.
 */
@Composable
fun PlugSearchField(
  query: String,
  onQueryChange: (String) -> Unit,
  placeholder: String,
  clearContentDescription: String,
  modifier: Modifier = Modifier,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isFocused by interactionSource.collectIsFocusedAsState()
  val keyboardController = LocalSoftwareKeyboardController.current

  BasicTextField(
    value = query,
    onValueChange = onQueryChange,
    interactionSource = interactionSource,
    singleLine = true,
    keyboardOptions = KeyboardOptions(
      capitalization = KeyboardCapitalization.Sentences,
      autoCorrectEnabled = false,
      keyboardType = KeyboardType.Text,
      imeAction = ImeAction.Search,
    ),
    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
    modifier = modifier,
    decorationBox = { innerTextField ->
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = FieldShape)
          .border(
            width = 1.dp,
            color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
            shape = FieldShape,
          )
          .padding(start = 16.dp, end = 4.dp),
      ) {
        Box(
          contentAlignment = Alignment.CenterStart,
          modifier = Modifier
            .weight(1f)
            .padding(vertical = 15.dp),
        ) {
          if (query.isEmpty()) {
            Text(
              text = placeholder,
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          innerTextField()
        }
        if (query.isEmpty()) {
          // Same footprint as the clear button, so the field doesn't shift when typing starts.
          Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
            Icon(
              imageVector = Icons.Rounded.Search,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(IconSize),
            )
          }
        } else {
          IconButton(onClick = { onQueryChange("") }) {
            Icon(
              imageVector = Icons.Rounded.Clear,
              contentDescription = clearContentDescription,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(IconSize),
            )
          }
        }
      }
    },
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
private fun PlugSearchFieldPreview() {
  PlugBrainTheme(dynamicColor = false) {
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(24.dp),
    ) {
      PlugSearchField(
        query = "",
        onQueryChange = {},
        placeholder = "Search",
        clearContentDescription = "Clear search",
      )
      PlugSearchField(
        query = "Insta",
        onQueryChange = {},
        placeholder = "Search",
        clearContentDescription = "Clear search",
      )
    }
  }
}
