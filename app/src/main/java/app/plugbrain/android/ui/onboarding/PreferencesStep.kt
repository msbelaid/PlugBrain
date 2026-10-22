package app.plugbrain.android.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SignalCellularAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonTertiary
import app.plugbrain.android.ui.designsystem.components.layout.PlugStepScaffold
import app.plugbrain.android.ui.designsystem.components.listitem.PlugSelectableCard
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset
import app.plugbrain.android.ui.onboarding.presentation.OnboardingBlockIntervals
import app.plugbrain.android.ui.onboarding.presentation.OnboardingStep
import app.plugbrain.android.ui.theme.MathlockAppTheme

@Composable
fun PreferencesStep(
  blockInterval: Int,
  difficulty: DifficultyPreset?,
  onBlockIntervalSelected: (Int) -> Unit,
  onDifficultySelected: (DifficultyPreset) -> Unit,
  onFinish: () -> Unit,
  onSkip: () -> Unit,
) {
  PlugStepScaffold(
    title = stringResource(R.string.onboarding_preferences_title),
    currentStep = OnboardingStep.Preferences.progress,
    totalSteps = OnboardingStep.progressSteps,
    primaryCtaText = stringResource(R.string.onboarding_preferences_cta),
    onPrimaryCtaClick = onFinish,
    topAction = { PlugButtonTertiary(text = stringResource(R.string.onboarding_skip), onClick = onSkip) },
  ) {
    SectionTitle(stringResource(R.string.onboarding_preferences_interval))
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.selectableGroup(),
    ) {
      OnboardingBlockIntervals.forEach { minutes ->
        PlugSelectableCard(
          title = pluralStringResource(R.plurals.onboarding_preferences_interval_option, minutes, minutes),
          icon = Icons.Rounded.Schedule,
          isSelected = blockInterval == minutes,
          onClick = { onBlockIntervalSelected(minutes) },
        )
      }
    }
    SectionTitle(
      text = stringResource(R.string.onboarding_preferences_difficulty),
      modifier = Modifier.padding(top = 8.dp),
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.selectableGroup(),
    ) {
      DifficultyPreset.entries.forEach { preset ->
        val (title, description) = when (preset) {
          DifficultyPreset.Beginner ->
            R.string.onboarding_difficulty_beginner to R.string.onboarding_difficulty_beginner_description
          DifficultyPreset.Intermediate ->
            R.string.onboarding_difficulty_intermediate to R.string.onboarding_difficulty_intermediate_description
          DifficultyPreset.Expert ->
            R.string.onboarding_difficulty_expert to R.string.onboarding_difficulty_expert_description
        }
        PlugSelectableCard(
          title = stringResource(title),
          description = stringResource(description),
          icon = Icons.Rounded.SignalCellularAlt,
          isSelected = difficulty == preset,
          onClick = { onDifficultySelected(preset) },
        )
      }
    }
  }
}

@Composable
private fun SectionTitle(
  text: String,
  modifier: Modifier = Modifier,
) {
  Text(
    text = text,
    style = MaterialTheme.typography.titleLarge,
    color = MaterialTheme.colorScheme.onSurface,
    modifier = modifier.semantics { heading() },
  )
}

@Preview(showSystemUi = true)
@Composable
private fun PreferencesStepPreview() {
  MathlockAppTheme(dynamicColor = false) {
    PreferencesStep(
      blockInterval = 5,
      difficulty = DifficultyPreset.Beginner,
      onBlockIntervalSelected = {},
      onDifficultySelected = {},
      onFinish = {},
      onSkip = {},
    )
  }
}
