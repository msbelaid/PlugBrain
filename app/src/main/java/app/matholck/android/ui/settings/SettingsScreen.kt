package app.matholck.android.ui.settings

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import app.matholck.android.R
import app.matholck.android.model.InstalledApp
import coil3.compose.rememberAsyncImagePainter

@Composable
fun SettingsScreen(
  modifier: Modifier = Modifier,
  lockedApps: List<InstalledApp>,
  onBlockApplicationsClicked: () -> Unit,
) {
  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding(),
    topBar = { },
  ) { innerPadding ->
    LazyColumn(modifier.padding(innerPadding)) {
      item { SettingsTitle() }
      item { Authorizations() }
      item { HorizontalDivider(thickness = 1.dp) }
      item { BlockedApplications(lockedApps, onBlockApplicationsClicked) }
      item { HorizontalDivider(thickness = 1.dp) }
      item { Exercises() }
      item { HorizontalDivider(thickness = 1.dp) }
      item { Timing() }
    }
  }
}

@Composable
fun SettingsTitle() {
  Text(
    text = "MathLock",
    fontSize = 57.sp,
    textAlign = TextAlign.Center,
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
  )
}

@Composable
fun Timing() {
}

@Composable
fun Exercises() {
}

@Composable
fun BlockedApplications(
  lockedApps: List<InstalledApp>,
  onBlockApplicationsClicked: () -> Unit,
) {
  Column(
    modifier = Modifier.clickable {
      onBlockApplicationsClicked()
    },
  ) {
    Text(
      text = pluralStringResource(
        R.plurals.applications_blocked,
        lockedApps.size,
        lockedApps.size,
      ),
      fontSize = 32.sp,
      modifier = Modifier.padding(16.dp),
    )
    LazyRow(
      modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(lockedApps.toList()) { app ->
        Image(
          modifier = Modifier
            .width(24.dp),
          painter = rememberAsyncImagePainter(app.icon.toBitmap()),
          contentDescription = app.name,
        )
      }
    }
  }
}

@Composable
fun Authorizations() {
  Text(
    text = "Authorizations",
    fontSize = 32.sp,
    modifier = Modifier.padding(16.dp),
  )
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
  SettingsScreen(
    lockedApps = listOf(
      InstalledApp(
        name = "Youtube",
        packageName = "com.google.youtube",
        icon = AppCompatResources.getDrawable(
          LocalContext.current,
          R.drawable.ic_launcher_background,
        )!!,
        selected = true,
      ),
      InstalledApp(
        name = "MathLock",
        packageName = "com.mathlock.android",
        icon = AppCompatResources.getDrawable(
          LocalContext.current,
          R.drawable.ic_launcher_background,
        )!!,
        selected = true,
      ),
    ),
    onBlockApplicationsClicked = { },
  )
}
