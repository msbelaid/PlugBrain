package app.plugbrain.android.ui.mindifficulty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.sheetdrawer.PlugBottomSheetDrawer
import app.plugbrain.android.ui.designsystem.components.slider.PlugSnapSlider
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset

/** The levels the slider snaps to and labels: both ends of [levels] and the onboarding presets. */
fun difficultySnapValues(levels: IntRange): List<Int> =
  (listOf(levels.first, levels.last) + DifficultyPreset.entries.map { it.minimalDifficulty })
    .filter { it in levels }
    .distinct()
    .sorted()

/**
 * Lets the user pick the level challenges start at, on a slider over [levels] that snaps to the
 * onboarding presets. [sampleChallenge] gives an example challenge for a level, shown as the
 * slider moves. Nothing is saved until [onSave].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinDifficultySheet(
  minDifficulty: Int,
  levels: IntRange,
  sampleChallenge: (Int) -> String?,
  onSave: (Int) -> Unit,
  onDismiss: () -> Unit,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
  var selected by rememberSaveable { mutableIntStateOf(minDifficulty.coerceIn(levels)) }
  val snapValues = remember(levels) { difficultySnapValues(levels) }
  // A new example only when the level changes, not on every recomposition.
  val sample = remember(selected) { sampleChallenge(selected) }
  PlugBottomSheetDrawer(
    title = {
      Text(
        text = stringResource(R.string.dashboard_min_difficulty),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
      )
    },
    primaryCtaText = stringResource(R.string.min_difficulty_sheet_save),
    primaryCtaAction = { onSave(selected) },
    secondaryCtaText = null,
    onDismiss = onDismiss,
    sheetState = sheetState,
  ) {
    Text(
      text = stringResource(R.string.min_difficulty_sheet_description),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text(
        text = difficultyLevelText(selected),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
      )
      if (sample != null) SampleChallenge(sample)
      val context = LocalContext.current
      PlugSnapSlider(
        value = selected,
        snapValues = snapValues,
        onValueChange = { selected = it },
        valueDescription = { context.getString(R.string.dashboard_min_difficulty_level, it) },
      )
    }
  }
}

@Composable
private fun SampleChallenge(challenge: String) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
      .padding(vertical = 12.dp, horizontal = 16.dp),
  ) {
    Text(
      text = stringResource(R.string.min_difficulty_sheet_example),
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Text(
      text = challenge,
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.onSurface,
    )
  }
}

/** "Intermediate · level 5" for a preset's level, "Level 7" otherwise; the dashboard row says the same. */
@Composable
fun difficultyLevelText(level: Int): String {
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
