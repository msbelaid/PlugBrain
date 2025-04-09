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
  installedApps: List<InstalledApp>,
  blockedApps: Set<String>,
  onBlockApplicationsClicked: () -> Unit,
) {
  Scaffold(
    modifier = Modifier.fillMaxSize().statusBarsPadding(),
    topBar = { },
  ) { innerPadding ->
    LazyColumn(modifier.padding(innerPadding)) {
      item { SettingsTitle() }
      item { Authorizations() }
      item { HorizontalDivider(thickness = 1.dp) }
      item { BlockedApplications(installedApps, blockedApps, onBlockApplicationsClicked) }
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
  installedApps: List<InstalledApp>,
  blockedApps: Set<String>,
  onBlockApplicationsClicked: () -> Unit,
) {
  Column(
    modifier = Modifier.clickable {
      onBlockApplicationsClicked()
    },
  ) {
    if (blockedApps.isEmpty()) {
      Text(
        text = "No application is blocked!",
        fontSize = 32.sp,
        modifier = Modifier.padding(16.dp),
      )
    } else {
      Text(
        text = pluralStringResource(
          R.plurals.applications_blocked,
          blockedApps.size,
          blockedApps.size,
        ),
        fontSize = 32.sp,
        modifier = Modifier.padding(16.dp),
      )
      LazyRow(
        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        items(blockedApps.toList()) { packageName ->
          val app = installedApps.firstOrNull { it.packageName == packageName }
          Image(
            modifier = Modifier
              .width(24.dp),
            painter = rememberAsyncImagePainter(app?.icon?.toBitmap()),
            contentDescription = app?.name,
          )
        }
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
    installedApps = listOf(
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
    blockedApps = setOf("com.google.youtube", "com.mathlock.android"),
    onBlockApplicationsClicked = { },
  )
}
