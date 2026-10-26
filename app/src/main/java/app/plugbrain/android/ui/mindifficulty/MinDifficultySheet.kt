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

/** The levels the slider snaps to, one per preset, labelled with the preset's name. */
val DifficultySnapValues = DifficultyPreset.entries.map { it.minimalDifficulty }

/**
 * Lets the user pick the level challenges start at, on a slider from Beginner to Expert that stops
 * on every level between them. [sampleChallenge] gives an example challenge for a level, shown as
 * the slider moves. Nothing is saved until [onSave].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinDifficultySheet(
  minDifficulty: Int,
  sampleChallenge: (Int) -> String?,
  onSave: (Int) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
  var selected by rememberSaveable {
    mutableIntStateOf(minDifficulty.coerceIn(DifficultySnapValues.first(), DifficultySnapValues.last()))
  }
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
    modifier = modifier,
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
        snapValues = DifficultySnapValues,
        onValueChange = { selected = it },
        valueDescription = { context.getString(R.string.dashboard_min_difficulty_level, it) },
        label = { level ->
          DifficultyPreset.fromMinimalDifficulty(level)?.let { context.getString(it.nameRes) }.orEmpty()
        },
        // Challenges can be anywhere from the selected level up to the hardest one.
        fillFromEnd = true,
      )
    }
  }
}

@Composable
private fun SampleChallenge(challenge: String) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
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

private val DifficultyPreset.nameRes: Int
  get() = when (this) {
    DifficultyPreset.Beginner -> R.string.onboarding_difficulty_beginner
    DifficultyPreset.Intermediate -> R.string.onboarding_difficulty_intermediate
    DifficultyPreset.Expert -> R.string.onboarding_difficulty_expert
  }

/** "Intermediate · level 5" for a preset's level, "Level 7" otherwise; the dashboard row says the same. */
@Composable
fun difficultyLevelText(level: Int): String {
  val preset = DifficultyPreset.fromMinimalDifficulty(level)
  return if (preset != null) {
    stringResource(R.string.dashboard_min_difficulty_preset, stringResource(preset.nameRes), level)
  } else {
    stringResource(R.string.dashboard_min_difficulty_level, level)
  }
}
