package app.plugbrain.android.ui.selectapps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.util.formatDuration
import coil3.compose.rememberAsyncImagePainter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Composable
fun InstalledAppItem(
  modifier: Modifier = Modifier,
  installedApp: InstalledApp,
  isSelected: Boolean,
  onClicked: (Boolean) -> Unit,
) {
  ListItem(
    headlineContent = {
      Text(text = installedApp.name)
    },
    supportingContent = {
      Text(text = usageLast24hText(installedApp.usageMillis))
    },
    leadingContent = {
      Image(
        modifier = Modifier.width(56.dp),
        painter = rememberAsyncImagePainter(model = installedApp.icon),
        contentDescription = installedApp.name,
      )
    },
    trailingContent = {
      Checkbox(
        checked = isSelected,
        onCheckedChange = null,
      )
    },
    modifier = modifier.toggleable(
      value = isSelected,
      onValueChange = onClicked,
      role = Role.Checkbox, // Important for accessibility
    ),
  )
}

@Composable
private fun usageLast24hText(usageMillis: Long): String {
  val usage = usageMillis.milliseconds
  return when {
    usage == Duration.ZERO -> stringResource(R.string.app_not_used_last_24h)
    usage < 1.minutes -> stringResource(R.string.app_used_less_than_minute_last_24h)
    else -> stringResource(R.string.app_usage_last_24h, LocalContext.current.formatDuration(usage))
  }
}

@Preview
@Composable
private fun InstalledAppPreview() {
  Column {
    InstalledAppItem(
      installedApp = InstalledApp(
        name = "Youtube",
        packageName = "com.google.youtube",
        icon = LocalContext.current.getDrawable(R.mipmap.ic_launcher)!!,
        usageMillis = 72 * 60_000L,
      ),
      onClicked = { },
      isSelected = true,
    )
    InstalledAppItem(
      installedApp = InstalledApp(
        name = "MathLock",
        packageName = "app.mathlock.android",
        icon = LocalContext.current.getDrawable(R.mipmap.ic_launcher)!!,
      ),
      isSelected = false,
      onClicked = { },
    )
  }
}
