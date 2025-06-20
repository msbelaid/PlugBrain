package app.plugbrain.android.ui.settings.compose

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import app.plugbrain.android.R
import app.plugbrain.android.repository.model.ChallengeSettings
import app.plugbrain.android.repository.model.Difficulty
import app.plugbrain.android.repository.model.InstalledApp
import app.plugbrain.android.repository.model.Operator
import app.plugbrain.android.ui.settings.presentation.PermissionsState
import coil3.compose.rememberAsyncImagePainter

@Composable
fun SettingsScreen(
  modifier: Modifier = Modifier,
  lockedApps: List<InstalledApp>,
  permissionsState: PermissionsState,
  challengeSettings: ChallengeSettings,
  blockInterval: Int,
  onBlockApplicationsClicked: () -> Unit,
  onAccessibilityClicked: () -> Unit,
  onUsageStatsClicked: () -> Unit,
  batteryOptimizationClicked: () -> Unit,
  onSystemAlertWindow: () -> Unit,
  onUpdateBlockInterval: (Int) -> Unit,
  onDifficultySelected: (Difficulty) -> Unit,
  onOperationSelected: (Operator) -> Unit,
  onRefreshClicked: () -> Unit,
) {
  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding(),
    topBar = { },
  ) { innerPadding ->
    LazyColumn(modifier.padding(innerPadding)) {
      item { SettingsTitle() }
      item { HorizontalDivider(thickness = 8.dp, modifier = Modifier.padding(bottom = 16.dp)) }
      item {
        Authorizations(
          permissionsState = permissionsState,
          onAccessibilityClicked = onAccessibilityClicked,
          onUsageStatsClicked = onUsageStatsClicked,
          batteryOptimizationClicked = batteryOptimizationClicked,
          onSystemAlertWindow = onSystemAlertWindow,
        )
      }
      item { HorizontalDivider(thickness = 8.dp) }
      item { BlockedApplications(lockedApps, onBlockApplicationsClicked) }
      item { HorizontalDivider(thickness = 8.dp) }
      item { ProgressiveDifficulty { onRefreshClicked() } }
      item { HorizontalDivider(thickness = 8.dp) }
      item { Timing(blockInterval, onUpdateBlockInterval) }
    }
  }
}

@Composable
fun ProgressiveDifficulty(
  modifier: Modifier = Modifier,
  onRefreshClicked: () -> Unit,
) {
  Box(modifier.fillMaxWidth()) {
    Button(onClick = onRefreshClicked, Modifier.align(Alignment.Center)) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Icon(
          imageVector = Icons.Filled.Refresh,
          contentDescription = "Refresh",
          modifier = Modifier.size(FilterChipDefaults.IconSize),
        )
        Text("Refresh")
      }
    }
  }
}

@Composable
fun SettingsTitle() {
  Text(
    text = "MathLock",
    style = MaterialTheme.typography.displayMedium,
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 16.dp, start = 32.dp, bottom = 16.dp),
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Timing(
  blockInterval: Int,
  onUpdateBlockInterval: (Int) -> Unit,
) {
  val options = listOf(1, 2, 5, 10, 15, 20, 30)

  Column {
    Text(
      text = "Block Applications every: $blockInterval mins",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(start = 32.dp, bottom = 16.dp, end = 16.dp),
    )
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.horizontalScroll(rememberScrollState()),
    ) {
      Spacer(Modifier.width(24.dp))
      options.forEach { value ->
        FilterChip(
          selected = blockInterval == value,
          onClick = { onUpdateBlockInterval(value) },
          label = { Text("$value min") },
          leadingIcon =
          {
            if (blockInterval == value) {
              Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = "Done icon",
                modifier = Modifier.size(FilterChipDefaults.IconSize),
              )
            }
          },
        )
      }
    }
  }
}

@Composable
fun BlockedApplications(
  lockedApps: List<InstalledApp>,
  onBlockApplicationsClicked: () -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .clickable {
          onBlockApplicationsClicked()
        },
    ) {
      Text(
        text = pluralStringResource(
          R.plurals.applications_blocked,
          lockedApps.size,
          lockedApps.size,
        ),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 32.dp, top = 16.dp, bottom = 8.dp),
      )
      LazyRow(
        modifier = Modifier.padding(start = 32.dp, bottom = 16.dp),
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
    Icon(
      imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
      contentDescription = "Open",
      modifier = Modifier.padding(end = 16.dp),
    )
  }
}

@Composable
fun PermissionItem(
  title: String,
  subtitle: String,
  granted: Boolean,
  @DrawableRes icon: Int,
  onClick: () -> Unit,
) {
  AnimatedVisibility(
    visible = granted.not(),
  ) {
    ListItem(
      headlineContent = {
        Text(text = title)
      },
      supportingContent = {
        Text(text = subtitle)
      },
      leadingContent = {
        Icon(
          modifier = Modifier.width(56.dp),
          painter = painterResource(icon),
          tint = MaterialTheme.colorScheme.onBackground,
          contentDescription = "",
        )
      },
      trailingContent = {
        if (granted) {
          Icon(Icons.Outlined.Check, "")
        } else {
          Icon(Icons.Outlined.Clear, "")
        }
      },
      modifier = Modifier.clickable(enabled = granted.not()) { onClick() },
    )
  }
}

@Composable
fun Authorizations(
  permissionsState: PermissionsState,
  onAccessibilityClicked: () -> Unit,
  onUsageStatsClicked: () -> Unit,
  batteryOptimizationClicked: () -> Unit,
  onSystemAlertWindow: () -> Unit,
) {
  if (permissionsState.areAllPermissionsGranted()) return

  Column {
    Text(
      text = "Authorizations",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(start = 32.dp),
    )
    PermissionItem(
      title = "Accessibility permission",
      subtitle = "More details about it",
      granted = permissionsState.accessibilityPermission,
      icon = R.drawable.baseline_accessibility_new_24,
      onClick = onAccessibilityClicked,
    )
    PermissionItem(
      title = "Usage stats",
      subtitle = "More details about it",
      granted = permissionsState.usageStatsPermission,
      icon = R.drawable.baseline_query_stats_24,
      onClick = onUsageStatsClicked,
    )
    PermissionItem(
      title = "System Alert Window",
      subtitle = "More details about it",
      granted = permissionsState.systemAlertWindow,
      icon = R.drawable.baseline_apps_outage_24,
      onClick = onSystemAlertWindow,
    )
    PermissionItem(
      title = "Battery Optimization Exemption",
      subtitle = "More details about it",
      granted = permissionsState.batteryOptimizationExemption,
      icon = R.drawable.baseline_battery_2_bar_24,
      onClick = batteryOptimizationClicked,
    )
  }
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
    permissionsState = PermissionsState(true, true, false, false),
    challengeSettings = ChallengeSettings(),
    blockInterval = 10,
    onBlockApplicationsClicked = { },
    onAccessibilityClicked = { },
    onUsageStatsClicked = { },
    batteryOptimizationClicked = { },
    onSystemAlertWindow = { },
    onUpdateBlockInterval = { },
    onOperationSelected = { },
    onDifficultySelected = { },
    onRefreshClicked = { },
  )
}
