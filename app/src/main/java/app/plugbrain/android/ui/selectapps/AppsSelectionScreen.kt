package app.plugbrain.android.ui.selectapps

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import app.plugbrain.android.R
import app.plugbrain.android.repository.model.InstalledApp

@Composable
fun AppsSelectionScreen(
  modifier: Modifier = Modifier,
  installedApps: List<InstalledApp>,
  selectedApps: Set<String>,
  onItemClicked: (String) -> Unit,
) {
  var searchQuery by remember { mutableStateOf("") }

  LazyColumn(modifier) {
    item {
      TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth().statusBarsPadding(),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
      )
    }
    items(
      installedApps
        .map { it.copy(selected = it.packageName in selectedApps) }
        .filter { it.name.contains(searchQuery) || it.packageName.contains(searchQuery) },
    ) {
      InstalledAppItem(installedApp = it) {
        onItemClicked(it.packageName)
      }
    }
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
)
@Composable
private fun AppsSelectionScreenPreview() {
  AppsSelectionScreen(
    installedApps = listOf(
      InstalledApp(
        name = "Youtube",
        packageName = "com.google.youtube",
        icon = AppCompatResources.getDrawable(
          LocalContext.current,
          R.mipmap.ic_launcher,
        )!!,
        selected = true,
      ),
      InstalledApp(
        name = "MathLock",
        packageName = "com.mathlock.android",
        icon = AppCompatResources.getDrawable(
          LocalContext.current,
          R.mipmap.ic_launcher,
        )!!,
        selected = true,
      ),
    ),
    selectedApps = setOf("com.google.youtube"),
  ) { }
}
