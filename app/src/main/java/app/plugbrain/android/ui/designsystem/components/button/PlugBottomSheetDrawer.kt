package app.plugbrain.android.ui.designsystem.components.button

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.theme.MathlockAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlugBottomSheetDrawer(
  modifier: Modifier = Modifier,
  title: (@Composable ColumnScope.() -> Unit)? = null,
  primaryCtaText: String?,
  primaryCtaAction: () -> Unit = { },
  primaryCtaIcon: ImageVector? = null,
  secondaryCtaText: String?,
  secondaryCtaAction: () -> Unit = { },
  secondaryCtaIcon: ImageVector? = null,
  onDismiss: () -> Unit = { },
  sheetState: SheetState = rememberModalBottomSheetState(),
  content: (@Composable ColumnScope.() -> Unit)? = null,
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface,
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 32.dp, start = 32.dp, end = 32.dp),
    ) {
      if (title != null) title()
      if (content != null) content()
      if (primaryCtaText != null) {
        PlugButtonPrimary(
          text = primaryCtaText,
          icon = primaryCtaIcon,
          onClick = primaryCtaAction,
          modifier = Modifier.fillMaxWidth(),
        )
      }
      if (secondaryCtaText != null) {
        PlugButtonSecondary(
          text = secondaryCtaText,
          icon = secondaryCtaIcon,
          onClick = secondaryCtaAction,
          modifier = Modifier.fillMaxWidth(),
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
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
fun PlugBottomSheetDrawerFormPreview() {
  var showSheet by remember { mutableStateOf(true) }
  val sheetState = rememberModalBottomSheetState()

  LaunchedEffect(Unit) { sheetState.expand() }

  MathlockAppTheme(dynamicColor = false) {
    if (showSheet) {
      PlugBottomSheetDrawer(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector = Icons.Rounded.Check,
              tint = MaterialTheme.colorScheme.tertiary,
              contentDescription = stringResource(R.string.settings_title),
              modifier = Modifier
                .size(40.dp)
                .padding(end = 8.dp),
            )
            Text(
              text = "Correct Answer",
              style = MaterialTheme.typography.headlineMedium,
              textAlign = TextAlign.Center,
            )
          }
        },
        primaryCtaText = "Unlock app for 5 mins",
        primaryCtaAction = { },
        primaryCtaIcon = Icons.Rounded.Clear,
        secondaryCtaText = "Stay Focused!",
        secondaryCtaAction = { },
        secondaryCtaIcon = Icons.Rounded.Check,
        onDismiss = { showSheet = false },
        sheetState = sheetState,
      ) {
        Text(
          text = "Click to unlock the app, or better : leave the app",
          style = MaterialTheme.typography.bodyLarge,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .align(Alignment.CenterHorizontally),
        )
      }
    }
  }
}
