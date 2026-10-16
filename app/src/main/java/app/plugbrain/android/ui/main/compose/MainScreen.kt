package app.plugbrain.android.ui.main.compose

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.repository.model.InstalledApp
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.main.presentation.MainScreenState
import app.plugbrain.android.util.formatDuration
import coil3.compose.rememberAsyncImagePainter
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  state: MainScreenState?,
  permissionsState: PermissionsState?,
  onSettingsClicked: () -> Unit,
) {
  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding(),
    topBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
          IconButton(onClick = {
            onSettingsClicked()
          }) {
            Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = stringResource(R.string.settings_title),
            )
          }
        },
      )
    },
  ) { innerPadding ->
    // TODO Display the missing permissions
    if (state != null && permissionsState?.areAllImportantPermissionsGranted() == true) {
      MainContent(Modifier.padding(innerPadding), state)
    }
  }
}

@Composable
private fun MainContent(
  modifier: Modifier,
  state: MainScreenState,
) {
  Column(modifier) {
    Text(
      text = stringResource(R.string.app_usage_description),
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(horizontal = 16.dp),
    )

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
          text = stringResource(R.string.settings_title),
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
              modifier = Modifier.width(48.dp),
              painter = rememberAsyncImagePainter(app.icon),
              contentDescription = app.name,
            )
          }
        }
      }
    }
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
          text = recapMessage(state),
          style = MaterialTheme.typography.titleMedium,
        )
        val usageDurationMinutes = state.lastUsageDuration?.inWholeMinutes ?: 0
        val formattedLastUsage = state.lastUsageDuration?.let {
          LocalContext.current.formatDuration(it)
        } ?: stringResource(R.string.duration_zero_minutes)
        Text(stringResource(R.string.stats_last_usage_time, formattedLastUsage, state.blockInterval))
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
        Text(stringResource(R.string.stats_hardness, state.difficultyLevel, state.maxDifficulty))
        LinearProgressIndicator(
          progress = {
            state.difficultyLevel.toFloat() / state.maxDifficulty
          },
          gapSize = 0.dp,
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .padding(horizontal = 24.dp),
        )
      }
    }
    Text(
      text = "This is a placeholder screen and will be redesigned later.",
      style = MaterialTheme.typography.labelSmall,
      modifier = Modifier
        .padding(top = 16.dp)
        .align(Alignment.CenterHorizontally),
    )
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
            R.mipmap.ic_launcher,
          )!!,
        ),
        InstalledApp(
          name = "MathLock",
          packageName = "com.plugbrain.android",
          icon = AppCompatResources.getDrawable(
            LocalContext.current,
            R.mipmap.ic_launcher,
          )!!,
        ),
      ),
      lastUsageDuration = 100_000L.milliseconds,
      usageFreeDuration = 900_000L.milliseconds,
      blockInterval = 5,
    ),
    permissionsState = PermissionsState(
      accessibilityPermission = true,
      usageStatsPermission = true,
      batteryOptimizationExemption = true,
      systemAlertWindow = true,
    ),
    onSettingsClicked = {},

  )
}

// TODO : Recap Usage stats
//         - Example 1: You have used distracting apps for more than X hours today!
//         - Example 2: You stayed away from distracting for more than one day and 3 hours
//         - Example 3: You have used distracting apps for 15min in the last session
//         - Example 4: Distracting Apps are blocked, hard challenge, stay away for easier challenges

@Composable
private fun recapMessage(state: MainScreenState): String {
  val context = LocalContext.current
  return when {
    (state.usageFreeDuration?.inWholeMinutes ?: 0) > state.blockInterval * 2 -> {
      val formattedDuration = state.usageFreeDuration?.let {
        context.formatDuration(it)
      } ?: stringResource(R.string.duration_zero_minutes)
      stringResource(R.string.usage_free_duration_message, formattedDuration)
    }
    (state.lastUsageDuration?.inWholeMinutes ?: 0) > 0 -> {
      val formattedDuration = state.lastUsageDuration?.let {
        context.formatDuration(it)
      } ?: stringResource(R.string.duration_zero_minutes)
      stringResource(R.string.apps_usage_duration_message, formattedDuration, state.blockInterval)
    }
    else -> ""
  }
}
