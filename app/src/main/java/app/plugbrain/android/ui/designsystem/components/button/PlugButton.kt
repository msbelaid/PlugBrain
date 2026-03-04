package app.plugbrain.android.ui.designsystem.components.button

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.theme.MathlockAppTheme
import app.plugbrain.android.ui.theme.PrimaryGradient

val shape = RoundedCornerShape(50)
val buttonPadding = PaddingValues(
  horizontal = 24.dp,
  vertical = 15.dp,
)

@Composable
fun PlugButtonPrimary(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  icon: ImageVector? = null,
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    shape = shape,
    contentPadding = buttonPadding,
    colors = ButtonDefaults.buttonColors(
      containerColor = Color.Transparent,
      disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    ),
    modifier = modifier
      .then(
        if (enabled) {
          Modifier.background(
            brush = PrimaryGradient,
            shape = shape,
          )
        } else {
          Modifier
        },
      ),
  ) {
    ButtonContent(
      text = text,
      icon = icon,
    )
  }
}

@Composable
fun PlugButtonSecondary(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  icon: ImageVector? = null,
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    shape = shape,
    contentPadding = buttonPadding,
  ) {
    ButtonContent(
      text = text,
      icon = icon,
    )
  }
}

@Composable
fun PlugButtonTertiary(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  icon: ImageVector? = null,
) {
  TextButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    shape = shape,
    contentPadding = buttonPadding,
  ) {
    ButtonContent(
      text = text,
      icon = icon,
    )
  }
}

@Composable
private fun ButtonContent(
  modifier: Modifier = Modifier,
  text: String,
  icon: ImageVector?,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    if (icon != null) {
      Icon(
        imageVector = icon,
        contentDescription = stringResource(R.string.settings_title),
        modifier = Modifier
          .padding(end = 8.dp)
          .size(18.dp),
      )
    }
    Text(
      text = text,
      style = MaterialTheme.typography.labelLarge,
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
private fun PlugButtonPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier.background(MaterialTheme.colorScheme.background),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      PlugButtonPrimary(
        text = "Primary Button with icon",
        onClick = {},
        icon = Icons.Default.Settings,
        modifier = Modifier.fillMaxWidth(),
      )
      PlugButtonPrimary(
        text = "Primary Button without icon",
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
      )
      PlugButtonPrimary(
        text = "Primary Button disabled",
        onClick = {},
        enabled = false,
        icon = Icons.Default.Settings,
        modifier = Modifier.fillMaxWidth(),
      )
      PlugButtonSecondary(
        text = "Secondary Button",
        onClick = {},
        icon = Icons.Default.Settings,
        modifier = Modifier.fillMaxWidth(),
      )
      PlugButtonSecondary(
        text = "Secondary Button",
        onClick = {},
        enabled = false,
        icon = Icons.Default.Settings,
        modifier = Modifier.fillMaxWidth(),
      )
      PlugButtonTertiary(
        text = "Tertiary Button",
        onClick = {},
        icon = Icons.Default.Settings,
        modifier = Modifier.fillMaxWidth(),
      )
      PlugButtonTertiary(
        text = "Tertiary Button",
        onClick = {},
        enabled = false,
        icon = Icons.Default.Settings,
        modifier = Modifier.fillMaxWidth(),
      )
    }
  }
}
