package app.matholck.android.ui.selectapps

import androidx.compose.foundation.layout.fillMaxWidth
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
import app.matholck.android.R
import app.matholck.android.ui.selectapps.model.InstalledApp

@Composable
fun AppsSelectionScreen(
  installedApps: List<InstalledApp>,
  selectedApps: Set<String>,
  onItemClicked: (String) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  LazyColumn {
    item {
      TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
      )
    }
    items(
      installedApps
        .map { it.copy(selected = it.packageName in selectedApps) }
        .filter { it.name.contains(searchQuery) || it.packageName.contains(searchQuery) }
    ) {
      InstalledAppItem(installedApp = it) {
        onItemClicked(it.packageName)
      }
    }
  }
}

@Preview
@Composable
private fun AppsSelectionScreenPreview() {
  AppsSelectionScreen(
    installedApps = listOf(
      InstalledApp(
        name = "Youtube",
        packageName = "com.google.youtube",
        icon = LocalContext.current.getDrawable(R.drawable.ic_launcher_background)!!,
        selected = true
      ),
      InstalledApp(
        name = "MathLock",
        packageName = "com.mathlock.android",
        icon = LocalContext.current.getDrawable(R.drawable.ic_launcher_background)!!,
        selected = true
      )
    ),
    selectedApps = setOf("com.google.youtube")
  ) { }
}
