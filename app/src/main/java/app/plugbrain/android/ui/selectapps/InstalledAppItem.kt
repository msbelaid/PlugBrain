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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.repository.model.InstalledApp
import coil3.compose.rememberAsyncImagePainter

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
      Text(text = installedApp.packageName)
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

@Preview
@Composable
private fun InstalledAppPreview() {
  Column {
    InstalledAppItem(
      installedApp = InstalledApp(
        name = "Youtube",
        packageName = "com.google.youtube",
        icon = LocalContext.current.getDrawable(R.mipmap.ic_launcher)!!,
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
