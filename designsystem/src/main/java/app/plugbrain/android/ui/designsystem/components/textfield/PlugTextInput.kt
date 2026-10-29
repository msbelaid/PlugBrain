package app.plugbrain.android.ui.designsystem.components.textfield

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.designsystem.components.textfield.PlugNumericalInputDefaults.ElevationFeedback
import app.plugbrain.android.ui.designsystem.components.textfield.PlugNumericalInputDefaults.ElevationFocused
import app.plugbrain.android.ui.designsystem.components.textfield.PlugNumericalInputDefaults.IconSize
import app.plugbrain.android.ui.designsystem.components.textfield.PlugNumericalInputDefaults.Shape
import app.plugbrain.android.ui.theme.PlugBrainTheme

@Composable
fun PlugNumericalInput(
  value: String,
  onValueChange: (String) -> Unit,
  onSubmit: (Int?) -> Unit,
  placeholder: String,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isSuccess: Boolean = false,
  isError: Boolean = false,
) {
  val focusBorderColor = when {
    isError -> MaterialTheme.colorScheme.error
    isSuccess -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.primary
  }
  val unfocusBorderColor = when {
    isError -> MaterialTheme.colorScheme.error
    isSuccess -> MaterialTheme.colorScheme.tertiary
    // A subtle border keeps the filled field from reading like an outlined button.
    else -> MaterialTheme.colorScheme.outlineVariant
  }
  val focusTextColor = when {
    isError -> MaterialTheme.colorScheme.error
    isSuccess -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.onSurface
  }
  val interactionSource = remember { MutableInteractionSource() }
  val isFocused by interactionSource.collectIsFocusedAsState()
  val elevation by animateDpAsState(
    when {
      isFocused -> ElevationFocused
      isSuccess -> ElevationFeedback
      isError -> ElevationFeedback
      else -> 0.dp
    },
    label = "anim_elevation",
  )

  BasicTextField(
    value = value,
    onValueChange = onValueChange,
    enabled = enabled,
    interactionSource = interactionSource,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Number,
      imeAction = ImeAction.Done,
    ),
    keyboardActions = KeyboardActions(
      onDone = { onSubmit(value.toIntOrNull()) },
    ),
    textStyle = MaterialTheme.typography.labelLarge.copy(
      textAlign = TextAlign.Center,
      color = focusTextColor,
    ),
    singleLine = true,
    modifier = modifier.then(
      if (isFocused || isSuccess || isError) {
        Modifier.shadow(
          elevation = elevation,
          ambientColor = focusBorderColor,
          spotColor = focusBorderColor,
          shape = Shape,
        )
      } else {
        Modifier
      },
    ),
    decorationBox = { innerTextField ->
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = Shape,
          )
          .border(
            width = 1.dp,
            color = if (isFocused) focusBorderColor else unfocusBorderColor,
            shape = Shape,
          )
          .padding(horizontal = 16.dp, vertical = 15.dp),
        contentAlignment = Alignment.Center,
      ) {
        if (value.isEmpty()) {
          Text(
            text = placeholder,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
          )
        }
        // The text is inset on both sides by the icon width so it stays centered and never runs under the icon.
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = IconSize),
          contentAlignment = Alignment.Center,
        ) { innerTextField() }
        // Sized to the text row and allowed to overflow it, so showing the icon doesn't change the field's height.
        Box(
          modifier = Modifier.matchParentSize(),
          contentAlignment = Alignment.CenterEnd,
        ) {
          PlugNumericalInputIcon(
            isSuccess = isSuccess,
            isError = isError,
            modifier = Modifier.requiredSize(IconSize),
          )
        }
      }
    },
  )
}

private object PlugNumericalInputDefaults {
  val Shape = RoundedCornerShape(50)
  val IconSize = 24.dp
  val ElevationFocused = 12.dp
  val ElevationFeedback = 6.dp
}

@Composable
private fun PlugNumericalInputIcon(
  isSuccess: Boolean,
  isError: Boolean,
  modifier: Modifier = Modifier,
) {
  when {
    isSuccess -> Icon(
      imageVector = Icons.Default.Check,
      tint = MaterialTheme.colorScheme.tertiary,
      contentDescription = "Input valid",
      modifier = modifier,
    )
    isError -> Icon(
      imageVector = Icons.Default.Clear,
      tint = MaterialTheme.colorScheme.error,
      contentDescription = "Input invalid",
      modifier = modifier,
    )
    else -> null
  }
}

@Preview(
  name = "Light Mode",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Font Scale",
  showBackground = true,
  fontScale = 2f,
)
@Preview(
  name = "Dark Mode",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugNumericalInputPreview() {
  val focusRequester = remember { FocusRequester() }

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

  PlugBrainTheme(dynamicColor = false) {
    Column(
      modifier = Modifier.background(MaterialTheme.colorScheme.surface),
    ) {
      PlugNumericalInput(
        value = "",
        onValueChange = {},
        onSubmit = {},
        placeholder = "Unfocused",
        modifier = Modifier
          .padding(8.dp)
          .fillMaxWidth(),
      )
      PlugNumericalInput(
        value = "Focused",
        onValueChange = {},
        onSubmit = {},
        placeholder = "Focused",
        modifier = Modifier
          .focusRequester(focusRequester)
          .focusable()
          .padding(8.dp)
          .fillMaxWidth(),
      )
      PlugNumericalInput(
        value = "Success",
        onValueChange = {},
        onSubmit = {},
        placeholder = "Success",
        isSuccess = true,
        modifier = Modifier
          .padding(8.dp)
          .fillMaxWidth(),
      )
      PlugNumericalInput(
        value = "Error",
        onValueChange = {},
        onSubmit = {},
        placeholder = "Error",
        isError = true,
        modifier = Modifier
          .padding(8.dp)
          .fillMaxWidth(),
      )
    }
  }
}
