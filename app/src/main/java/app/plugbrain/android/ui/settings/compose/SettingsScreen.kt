package app.plugbrain.android.ui.settings.compose

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsCarryFreeChallenge
import app.plugbrain.android.repository.model.InstalledApp
import app.plugbrain.android.repository.model.PermissionsState
import coil3.compose.rememberAsyncImagePainter

@Composable
fun SettingsScreen(
  modifier: Modifier = Modifier,
  lockedApps: List<InstalledApp>,
  permissionsState: PermissionsState,
  blockInterval: Int,
  minDifficulty: Int,
  maxDifficulty: Int,
  selectedMinDifficulty: Int,
  minDifficultySample: Challenge,
  onBlockApplicationsClicked: () -> Unit,
  onAccessibilityClicked: () -> Unit,
  onUsageStatsClicked: () -> Unit,
  batteryOptimizationClicked: () -> Unit,
  onSystemAlertWindow: () -> Unit,
  onUpdateBlockInterval: (Int) -> Unit,
  onMinDifficultySelected: (Int) -> Unit,
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
      item { Timing(blockInterval, onUpdateBlockInterval) }
      item { HorizontalDivider(thickness = 8.dp, modifier = Modifier.padding(top = 16.dp)) }
      item {
        MinDifficulty(
          minimalDifficulty = minDifficulty,
          selectedMinDifficulty = selectedMinDifficulty,
          maximalDifficulty = maxDifficulty,
          minimalDifficultySample = minDifficultySample,
          onMinDifficultySelected = onMinDifficultySelected,
        )
      }
    }
  }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MinDifficulty(
  selectedMinDifficulty: Int,
  minimalDifficulty: Int,
  maximalDifficulty: Int,
  minimalDifficultySample: Challenge,
  onMinDifficultySelected: (Int) -> Unit,
) {
  Column(
    modifier = Modifier.padding(horizontal = 32.dp),
  ) {
    Text(
      text = stringResource(R.string.minimal_difficulty),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(bottom = 8.dp, top = 16.dp),
    )
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
      val percent =
        (selectedMinDifficulty - minimalDifficulty).toFloat() / (maximalDifficulty - minimalDifficulty)
      val offsetX = (maxWidth - 80.dp) * percent
      Text(
        text = minimalDifficultySample.string(),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
          .offset(x = offsetX)
          .align(Alignment.CenterStart),
      )
    }
    Slider(
      value = selectedMinDifficulty.toFloat(),
      onValueChange = { value ->
        onMinDifficultySelected(value.toInt())
      },
      valueRange = minimalDifficulty.toFloat()..maximalDifficulty.toFloat(),
      colors = SliderDefaults.colors(
        activeTrackColor = MaterialTheme.colorScheme.inversePrimary,
        inactiveTrackColor = MaterialTheme.colorScheme.primary,
        inactiveTickColor = MaterialTheme.colorScheme.inversePrimary,
        thumbColor = MaterialTheme.colorScheme.primary,
      ),
      steps = maximalDifficulty - minimalDifficulty - 1,
    )
  }
}

@Composable
fun SettingsTitle() {
  Text(
    text = stringResource(R.string.app_name),
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
  val options = listOf(1, 2, 5, 10, 15, 20)

  Column {
    Text(
      text = stringResource(R.string.blocking_interval),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(start = 32.dp, bottom = 16.dp, top = 16.dp, end = 16.dp),
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
          label = { Text(value.toString() + " " + stringResource(R.string.minutes_short)) },
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
    modifier = Modifier.clickable { onBlockApplicationsClicked() },
  ) {
    Column(modifier = Modifier.weight(1f)) {
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
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        items(lockedApps.toList()) { app ->
          Image(
            modifier = Modifier.width(48.dp),
            painter = rememberAsyncImagePainter(app.icon),
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
  if (permissionsState.areAllPermissionsGranted()) {
    Text(
      text = stringResource(R.string.all_permissions_granted),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(start = 32.dp, bottom = 16.dp),
    )
    return
  }

  Column {
    Text(
      text = stringResource(R.string.permissions_title),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(start = 32.dp),
    )
    PermissionItem(
      title = stringResource(R.string.accessibility_permission),
      subtitle = stringResource(R.string.accessibility_permission_description),
      granted = permissionsState.accessibilityPermission,
      icon = R.drawable.baseline_accessibility_new_24,
      onClick = onAccessibilityClicked,
    )
    PermissionItem(
      title = stringResource(R.string.usage_access_permission),
      subtitle = stringResource(R.string.usage_access_permission_description),
      granted = permissionsState.usageStatsPermission,
      icon = R.drawable.baseline_query_stats_24,
      onClick = onUsageStatsClicked,
    )
    PermissionItem(
      title = stringResource(R.string.display_over_apps_permission),
      subtitle = stringResource(R.string.display_over_apps_permission_description),
      granted = permissionsState.systemAlertWindow,
      icon = R.drawable.baseline_apps_outage_24,
      onClick = onSystemAlertWindow,
    )
    PermissionItem(
      title = stringResource(R.string.battery_optimization_exemption_permission),
      subtitle = stringResource(R.string.battery_optimization_exemption_permission_description),
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
          R.mipmap.ic_launcher,
        )!!,
      ),
      InstalledApp(
        name = stringResource(R.string.app_name),
        packageName = "app.plugbrain.android",
        icon = AppCompatResources.getDrawable(
          LocalContext.current,
          R.mipmap.ic_launcher,
        )!!,
      ),
    ),
    permissionsState = PermissionsState(
      accessibilityPermission = true,
      usageStatsPermission = true,
      batteryOptimizationExemption = false,
      systemAlertWindow = false,
    ),
    blockInterval = 10,
    selectedMinDifficulty = 3,
    minDifficulty = 0,
    maxDifficulty = 15,
    minDifficultySample = AdditionTwoDigitsCarryFreeChallenge(),
    onBlockApplicationsClicked = { },
    onAccessibilityClicked = { },
    onUsageStatsClicked = { },
    batteryOptimizationClicked = { },
    onSystemAlertWindow = { },
    onUpdateBlockInterval = { },
    onMinDifficultySelected = { },
  )
}
