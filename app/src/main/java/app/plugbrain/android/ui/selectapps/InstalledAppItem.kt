package app.plugbrain.android.ui.selectapps

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import app.plugbrain.android.R
import app.plugbrain.android.repository.model.InstalledApp
import coil3.compose.rememberAsyncImagePainter

@Composable
fun InstalledAppItem(
  modifier: Modifier = Modifier,
  installedApp: InstalledApp,
  onClicked: () -> Unit,
) {
  ListItem(
    headlineContent = {
      Text(text = installedApp.name)
    },
    supportingContent = {
      Text(text = installedApp.packageName)
    },
    leadingContent = {
      Image(
        modifier = Modifier.width(56.dp),
        painter = rememberAsyncImagePainter(installedApp.icon.toBitmap()),
        contentDescription = "",
      )
    },
    trailingContent = {
      Checkbox(
        checked = installedApp.selected,
        onCheckedChange = { onClicked() },
      )
    },
    modifier = modifier.clickable { onClicked() },
  )
}

@Preview
@Composable
private fun InstalledAppPreview() {
  Column {
    InstalledAppItem(
      installedApp = InstalledApp(
        name = "Youtube",
        packageName = "com.google.youtube",
        icon = LocalContext.current.getDrawable(R.drawable.ic_launcher_background)!!,
        selected = true,
      ),
      onClicked = { },
    )
    InstalledAppItem(
      installedApp = InstalledApp(
        name = "MathLock",
        packageName = "app.mathlock.android",
        icon = LocalContext.current.getDrawable(R.drawable.ic_launcher_background)!!,
      ),
      onClicked = { },
    )
  }
}
