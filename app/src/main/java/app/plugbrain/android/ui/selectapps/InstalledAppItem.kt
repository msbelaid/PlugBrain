package app.plugbrain.android.ui.selectapps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.appsusage.model.InstalledApp
import coil3.compose.rememberAsyncImagePainter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Composable
fun InstalledAppItem(
  installedApp: InstalledApp,
  usageFraction: Float,
  isSelected: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  ListItem(
    headlineContent = {
      Text(text = installedApp.name)
    },
    supportingContent = if (installedApp.usageMillis > 0) {
      { UsageBar(usageMillis = installedApp.usageMillis, fraction = usageFraction) }
    } else {
      null
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
      onValueChange = onCheckedChange,
      role = Role.Checkbox, // Important for accessibility
    ),
  )
}

/** The app's usage in the last 24h as a bar scaled to the most used app, followed by a short time. */
@Composable
internal fun UsageBar(usageMillis: Long, fraction: Float) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    LinearProgressIndicator(
      progress = { fraction },
      gapSize = 0.dp,
      drawStopIndicator = {},
      // Fixed width so every row's track has the same length, whatever the time text says.
      // In a narrow row it shrinks to leave room for the time.
      modifier = Modifier
        .weight(1f, fill = false)
        .width(160.dp)
        .height(6.dp),
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = shortUsage(usageMillis.milliseconds),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun shortUsage(usage: Duration): String {
  val hours = usage.inWholeHours.toInt()
  val minutes = (usage.inWholeMinutes % 60).toInt()
  return when {
    usage < 1.minutes -> stringResource(R.string.usage_short_less_than_minute)
    hours == 0 -> stringResource(R.string.usage_short_minutes, minutes)
    minutes == 0 -> stringResource(R.string.usage_short_hours, hours)
    else -> stringResource(R.string.usage_short_hours_minutes, hours, minutes)
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
        usageMillis = 104 * 60_000L,
      ),
      usageFraction = 1f,
      onCheckedChange = { },
      isSelected = true,
    )
    InstalledAppItem(
      installedApp = InstalledApp(
        name = "MathLock",
        packageName = "app.mathlock.android",
        icon = LocalContext.current.getDrawable(R.mipmap.ic_launcher)!!,
      ),
      usageFraction = 0f,
      isSelected = false,
      onCheckedChange = { },
    )
  }
}
