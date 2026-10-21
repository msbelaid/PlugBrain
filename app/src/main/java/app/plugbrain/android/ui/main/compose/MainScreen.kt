package app.plugbrain.android.ui.main.compose

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SignalCellularAlt
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.card.PlugStatusCard
import app.plugbrain.android.ui.designsystem.components.card.PlugStatusCardVariant
import app.plugbrain.android.ui.designsystem.components.listitem.PlugAppIconStack
import app.plugbrain.android.ui.designsystem.components.listitem.PlugNavigationListItem
import app.plugbrain.android.ui.main.presentation.DashboardStatus
import app.plugbrain.android.ui.main.presentation.MainScreenState
import app.plugbrain.android.ui.main.presentation.RequiredPermission
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset

/**
 * The dashboard: what PlugBrain is doing right now, then the user's setup. When PlugBrain is
 * paused, [onTurnBackOn] gets the missing permission so the caller can explain and request it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  state: MainScreenState?,
  onSettingsClicked: () -> Unit,
  onAppsClicked: () -> Unit,
  onBlockIntervalClicked: () -> Unit,
  onTurnBackOn: (RequiredPermission) -> Unit,
) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
          )
        },
        actions = {
          IconButton(onClick = onSettingsClicked) {
            Icon(
              imageVector = Icons.Rounded.Settings,
              contentDescription = stringResource(R.string.settings_title),
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
      )
    },
    containerColor = MaterialTheme.colorScheme.background,
  ) { innerPadding ->
    if (state != null) {
      Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
          .padding(innerPadding)
          .verticalScroll(rememberScrollState())
          .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
      ) {
        StatusCard(
          status = state.status,
          onAppsClicked = onAppsClicked,
          onTurnBackOn = onTurnBackOn,
        )
        SetupSection(
          state = state,
          onSettingsClicked = onSettingsClicked,
          onAppsClicked = onAppsClicked,
          onBlockIntervalClicked = onBlockIntervalClicked,
        )
      }
    }
  }
}

@Composable
private fun StatusCard(
  status: DashboardStatus,
  onAppsClicked: () -> Unit,
  onTurnBackOn: (RequiredPermission) -> Unit,
) {
  val modifier = Modifier.fillMaxWidth()
  when (status) {
    is DashboardStatus.Active -> PlugStatusCard(
      label = stringResource(R.string.dashboard_active),
      icon = Icons.Rounded.Psychology,
      headline = stringResource(R.string.dashboard_minutes_short, status.minutesLeft),
      headlineSupportingText = stringResource(R.string.dashboard_minutes_left),
      progress = status.usedFraction,
      body = stringResource(R.string.dashboard_next_challenge_level, status.nextChallengeLevel, status.maxChallengeLevel),
      modifier = modifier,
    )
    is DashboardStatus.AppsLocked -> PlugStatusCard(
      label = stringResource(R.string.dashboard_apps_locked),
      icon = Icons.Rounded.Lock,
      headline = status.awayMinutes?.let { stringResource(R.string.dashboard_away, shortDuration(it)) },
      body = stringResource(R.string.dashboard_unlock_level, status.challengeLevel),
      modifier = modifier,
    )
    is DashboardStatus.Paused -> PlugStatusCard(
      label = stringResource(R.string.dashboard_paused),
      icon = Icons.Rounded.WarningAmber,
      variant = PlugStatusCardVariant.Warning,
      body = stringResource(
        when (status.missingPermission) {
          RequiredPermission.Accessibility -> R.string.dashboard_paused_accessibility
          RequiredPermission.UsageAccess -> R.string.dashboard_paused_usage_access
          RequiredPermission.Overlay -> R.string.dashboard_paused_overlay
        },
      ),
      actionText = stringResource(R.string.dashboard_turn_back_on),
      onAction = { onTurnBackOn(status.missingPermission) },
      modifier = modifier,
    )
    is DashboardStatus.NothingToBlock -> PlugStatusCard(
      label = stringResource(R.string.dashboard_nothing_to_block),
      icon = Icons.Rounded.Apps,
      body = pluralStringResource(
        R.plurals.dashboard_nothing_to_block_description,
        status.blockInterval,
        status.blockInterval,
      ),
      actionText = stringResource(R.string.dashboard_choose_apps),
      onAction = onAppsClicked,
      modifier = modifier,
    )
  }
}

@Composable
private fun SetupSection(
  state: MainScreenState,
  onSettingsClicked: () -> Unit,
  onAppsClicked: () -> Unit,
  onBlockIntervalClicked: () -> Unit,
) {
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(
      text = stringResource(R.string.dashboard_your_setup),
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier
        .padding(bottom = 4.dp)
        .semantics { heading() },
    )
    if (state.blockedApps.isEmpty()) {
      PlugNavigationListItem(
        title = stringResource(R.string.dashboard_blocked_apps_title),
        description = stringResource(R.string.dashboard_blocked_apps_none),
        icon = Icons.Rounded.Apps,
        onClick = onAppsClicked,
      )
    } else {
      PlugNavigationListItem(
        title = pluralStringResource(
          R.plurals.dashboard_blocked_apps_count,
          state.blockedApps.size,
          state.blockedApps.size,
        ),
        icon = Icons.Rounded.Apps,
        onClick = onAppsClicked,
        supportingContent = {
          PlugAppIconStack(
            icons = state.blockedApps.take(MAX_VISIBLE_ICONS).map { appIconPainter(it.icon) },
            totalCount = state.blockedApps.size,
            maxVisible = MAX_VISIBLE_ICONS,
          )
        },
      )
    }
    PlugNavigationListItem(
      title = stringResource(R.string.dashboard_block_interval),
      description = pluralStringResource(
        R.plurals.dashboard_block_interval_value,
        state.blockInterval,
        state.blockInterval,
      ),
      icon = Icons.Rounded.Schedule,
      onClick = onBlockIntervalClicked,
    )
    PlugNavigationListItem(
      title = stringResource(R.string.dashboard_min_difficulty),
      description = minDifficultyText(state.minDifficulty),
      icon = Icons.Rounded.SignalCellularAlt,
      onClick = onSettingsClicked,
    )
  }
}

// The stack shows five icons and "+N" for the rest, so only those five are drawn.
private const val MAX_VISIBLE_ICONS = 5
private const val ICON_SIZE_PX = 96

/** App icons are already loaded drawables, so they are drawn right away instead of loading async. */
@Composable
private fun appIconPainter(icon: Drawable?): Painter {
  val fallback = rememberVectorPainter(Icons.Rounded.Android)
  return remember(icon) {
    icon?.let { BitmapPainter(it.toBitmap(ICON_SIZE_PX, ICON_SIZE_PX).asImageBitmap()) }
  } ?: fallback
}

@Composable
private fun minDifficultyText(level: Int): String {
  val presetName = when (DifficultyPreset.fromMinimalDifficulty(level)) {
    DifficultyPreset.Beginner -> R.string.onboarding_difficulty_beginner
    DifficultyPreset.Intermediate -> R.string.onboarding_difficulty_intermediate
    DifficultyPreset.MathMaster -> R.string.onboarding_difficulty_math_master
    null -> null
  }
  return if (presetName != null) {
    stringResource(R.string.dashboard_min_difficulty_preset, stringResource(presetName), level)
  } else {
    stringResource(R.string.dashboard_min_difficulty_level, level)
  }
}

@Composable
private fun shortDuration(minutes: Long): String {
  val hours = minutes / 60
  return if (hours == 0L) {
    stringResource(R.string.dashboard_minutes_short, minutes)
  } else {
    stringResource(R.string.dashboard_hours_minutes_short, hours, minutes % 60)
  }
}
