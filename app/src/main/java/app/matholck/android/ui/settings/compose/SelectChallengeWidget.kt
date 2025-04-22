package app.matholck.android.ui.settings.compose

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.matholck.android.repository.model.ChallengeSettings
import app.matholck.android.repository.model.Difficulty
import app.matholck.android.repository.model.Operator

@Composable
fun SelectChallengesWidget(
  modifier: Modifier = Modifier,
  challengeSettings: ChallengeSettings,
  onOperationSelected: (Operator) -> Unit,
  onDifficultySelected: (Difficulty) -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier,
  ) {
    Text("Operation", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(start = 24.dp))
    OperationSelection(
      challengeSettings = challengeSettings,
      onOperationSelected = onOperationSelected,
    )
    Text("Difficulty", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(start = 24.dp))
    DifficultySelection(
      challengeSettings = challengeSettings,
      onDifficultySelected = onDifficultySelected,
    )
  }
}

@Composable
private fun DifficultySelection(
  challengeSettings: ChallengeSettings,
  onDifficultySelected: (Difficulty) -> Unit,
) {
  val difficultyLevels = Difficulty.entries.toList()
  // TODO Allow multiselect
  Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.horizontalScroll(rememberScrollState()),
  ) {
    Spacer(Modifier.width(24.dp))
    difficultyLevels.forEach { level ->
      FilterChip(
        selected = challengeSettings.difficulty == level,
        onClick = { onDifficultySelected(level) },
        label = { Text(level.name) },
        leadingIcon =
        {
          if (challengeSettings.difficulty == level) {
            Icon(
              imageVector = Icons.Filled.Done,
              contentDescription = "Selected icon",
              modifier = Modifier.size(FilterChipDefaults.IconSize),
            )
          }
        },
      )
    }
  }
}

@Composable
private fun OperationSelection(
  modifier: Modifier = Modifier,
  challengeSettings: ChallengeSettings,
  onOperationSelected: (Operator) -> Unit,
) {
  val operations = Operator.entries.toList()
  Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier.horizontalScroll(rememberScrollState()),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(Modifier.width(24.dp))
    operations.forEach { operation ->
      FilterChip(
        selected = challengeSettings.operator == operation,
        onClick = { onOperationSelected(operation) },
        label = { Text("${operation.name} (${operation.symbol})") },
        leadingIcon =
        {
          if ((challengeSettings.operator == operation)) {
            Icon(
              imageVector = Icons.Filled.Done,
              contentDescription = "Selected icon",
              modifier = Modifier.size(FilterChipDefaults.IconSize),
            )
          }
        },
      )
    }
  }
}

@Preview
@Composable
private fun SelectChallengesPreview() {
  var selectedOperation by remember { mutableStateOf<Operator>(Operator.SUBTRACTION) }
  var selectedDifficulty by remember { mutableStateOf<Difficulty>(Difficulty.EASY) }

  SelectChallengesWidget(
    challengeSettings = ChallengeSettings(selectedOperation, selectedDifficulty),
    onOperationSelected = { operation -> selectedOperation = operation },
    onDifficultySelected = { difficulty -> selectedDifficulty = difficulty },
  )
}
