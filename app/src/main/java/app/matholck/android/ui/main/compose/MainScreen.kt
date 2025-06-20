package app.matholck.android.ui.main.compose

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import app.matholck.android.R
import app.matholck.android.repository.model.ChallengeSettings.Companion.challengeProgressionList
import app.matholck.android.repository.model.InstalledApp
import app.matholck.android.ui.main.presentation.MainScreenState
import coil3.compose.rememberAsyncImagePainter
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  state: MainScreenState?,
  onSettingsClicked: () -> Unit,
) {
  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding(),
    topBar = {
      TopAppBar(
        title = { Text("My App") },
        actions = {
          IconButton(onClick = {
            onSettingsClicked()
          }) {
            Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = "Settings",
            )
          }
        },
      )
    },
  ) { innerPadding ->
    if (state != null) {
      Column(Modifier.padding(innerPadding)) {
        // CARD 1: Recap Number of blocked apps every X minutes.
        Card(
          shape = RoundedCornerShape(16.dp),
          elevation = CardDefaults.cardElevation(8.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        ) {
          Column(Modifier.padding(16.dp)) {
            Text(
              text = "Blocked Apps",
              style = MaterialTheme.typography.titleLarge,
            )
            Text(
              // TODO add utils function for plural.
              text = LocalContext.current.resources.getQuantityString(
                R.plurals.applications_blocked,
                state.blockedApps.count(),
                state.blockedApps.count(),
              ) + " " + LocalContext.current.resources.getQuantityString(
                R.plurals.blocked_every_x_minutes,
                state.blockInterval,
                state.blockInterval,
              ),
              style = MaterialTheme.typography.titleMedium,
            )
            LazyRow(
              modifier = Modifier.padding(16.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              items(state.blockedApps.toList()) { app ->
                Image(
                  modifier = Modifier
                    .width(48.dp),
                  painter = rememberAsyncImagePainter(app.icon.toBitmap()),
                  contentDescription = app.name,
                )
              }
            }
            Text(
              text = "Unlock distraction apps with math\nThe less you use them -> the easier the challenges.",
              style = MaterialTheme.typography.bodyLarge,
            )
          }
        }
        // TODO CARD 2: Recap Usage stats
        //         - Example 1: You have used distracting apps for more than X hours today!
        //         - Example 2: You stayed away from distracting for more than one day and 3 hours
        //         - Example 3: You have used distracting apps for 15min in the last session
        Card(
          shape = RoundedCornerShape(16.dp),
          elevation = CardDefaults.cardElevation(8.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        ) {
          Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp),
          ) {
            Text(
              text = "Nice! You've stayed away from distracting apps for over ${state.usageFreeDuration}. Keep it up!",
              style = MaterialTheme.typography.titleMedium,
            )
            val usageDurationMinutes = state.lastUsageDuration?.inWholeMinutes ?: 0
            Text("Last usage Time: $usageDurationMinutes/${state.blockInterval}")
            LinearProgressIndicator(
              progress = {
                usageDurationMinutes.toFloat() / state.blockInterval
              },
              gapSize = 0.dp,
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(horizontal = 24.dp),
            )
            Text("Hardness: ${state.difficultyLevel}/${challengeProgressionList.count()}")
            LinearProgressIndicator(
              progress = {
                state.difficultyLevel.toFloat() / challengeProgressionList.count()
              },
              gapSize = 0.dp,
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(horizontal = 24.dp),
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun MainScreenPreview() {
  MainScreen(
    state = MainScreenState(
      blockedApps = setOf(
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
      lastUsageDuration = 100_000L.milliseconds,
      usageFreeDuration = 100_000L.milliseconds,
      blockInterval = 15,
    ),
    onSettingsClicked = {},

  )
}
