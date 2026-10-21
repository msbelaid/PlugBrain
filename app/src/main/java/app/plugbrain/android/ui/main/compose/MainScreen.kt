package app.plugbrain.android.ui.main.compose

import android.graphics.drawable.Drawable
import android.text.format.DateFormat
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
import app.plugbrain.android.appsusage.model.PhoneActivity
import app.plugbrain.android.ui.designsystem.components.card.PlugStatusCard
import app.plugbrain.android.ui.designsystem.components.card.PlugStatusCardVariant
import app.plugbrain.android.ui.designsystem.components.chart.PlugChartCard
import app.plugbrain.android.ui.designsystem.components.chart.PlugTimelineBand
import app.plugbrain.android.ui.designsystem.components.chart.PlugTimelineChart
import app.plugbrain.android.ui.designsystem.components.chart.PlugTimelineLabel
import app.plugbrain.android.ui.designsystem.components.chart.PlugTimelineLegend
import app.plugbrain.android.ui.designsystem.components.chart.PlugTimelineStep
import app.plugbrain.android.ui.designsystem.components.chart.PlugTimelineTone
import app.plugbrain.android.ui.designsystem.components.listitem.PlugAppIconStack
import app.plugbrain.android.ui.designsystem.components.listitem.PlugNavigationListItem
import app.plugbrain.android.ui.main.presentation.DashboardStatus
import app.plugbrain.android.ui.main.presentation.DifficultyTimeline
import app.plugbrain.android.ui.main.presentation.MainScreenState
import app.plugbrain.android.ui.main.presentation.RequiredPermission
import app.plugbrain.android.ui.main.presentation.timelineHourTicks
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        state.difficultyTimeline?.let { DifficultyTimelineCard(it) }
        SetupSection(
          state = state,
          onSettingsClicked = onSettingsClicked,
          onAppsClicked = onAppsClicked,
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
private fun DifficultyTimelineCard(timeline: DifficultyTimeline) {
  val currentLevel = timeline.currentLevel
  val timeFormat = remember { SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "j"), Locale.getDefault()) }
  val now = stringResource(R.string.dashboard_difficulty_now)
  val timeLabels = remember(timeline, now) {
    timelineHourTicks(timeline.start, timeline.end).map { PlugTimelineLabel(it, timeFormat.format(Date(it))) } +
      PlugTimelineLabel(timeline.end, now)
  }
  val highest = timeline.highestLevel ?: 0
  // Room above the highest level for the line and the dot, in steps of five like the grid.
  val maxValue = ((highest / VALUE_TICK_STEP) + 1) * VALUE_TICK_STEP
  PlugChartCard(
    title = stringResource(R.string.dashboard_difficulty_title),
    subtitle = stringResource(R.string.dashboard_difficulty_last_24_hours),
    badge = currentLevel?.let { stringResource(R.string.dashboard_difficulty_level_now, it) },
    modifier = Modifier.fillMaxWidth(),
  ) {
    PlugTimelineChart(
      startTime = timeline.start,
      endTime = timeline.end,
      steps = timeline.levels.map { PlugTimelineStep(it.timestamp, it.level) },
      bands = timeline.activity.map { PlugTimelineBand(it.start, it.end, it.activity.tone()) },
      maxValue = maxValue,
      valueTicks = listOf(1) + (VALUE_TICK_STEP..maxValue step VALUE_TICK_STEP),
      timeLabels = timeLabels,
      contentDescription = stringResource(
        R.string.dashboard_difficulty_description,
        timeline.lowestLevel ?: 0,
        highest,
        currentLevel ?: 0,
        shortDuration(timeline.millisSpent(PhoneActivity.Away) / ONE_MINUTE),
        shortDuration(timeline.millisSpent(PhoneActivity.DistractingApp) / ONE_MINUTE),
      ),
    )
    PlugTimelineLegend(
      items = listOf(
        PlugTimelineTone.Positive to stringResource(R.string.dashboard_difficulty_away),
        PlugTimelineTone.Negative to stringResource(R.string.dashboard_difficulty_distracting),
        PlugTimelineTone.Neutral to stringResource(R.string.dashboard_difficulty_other),
      ),
    )
  }
}

private fun PhoneActivity.tone() = when (this) {
  PhoneActivity.Away -> PlugTimelineTone.Positive
  PhoneActivity.DistractingApp -> PlugTimelineTone.Negative
  PhoneActivity.OtherApp -> PlugTimelineTone.Neutral
}

private const val VALUE_TICK_STEP = 5
private const val ONE_MINUTE = 60_000L

@Composable
private fun SetupSection(
  state: MainScreenState,
  onSettingsClicked: () -> Unit,
  onAppsClicked: () -> Unit,
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
      onClick = onSettingsClicked,
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
