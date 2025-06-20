package app.matholck.android.repository

import app.matholck.android.datastore.DataStoreManager
import app.matholck.android.repository.model.ChallengeSettings
import app.matholck.android.repository.model.Difficulty
import app.matholck.android.repository.model.MathChallenge
import app.matholck.android.repository.model.Operator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MathChallengeRepository(
  private val dataStoreManager: DataStoreManager,
) {
  fun generateProgressiveChallenge(): Flow<MathChallenge> {
    // TODO pass filtering as parameter, useful if we want to bypass some levels, for example start from medium level, or just use additions.
    val selectedOperators: Set<Operator> = Operator.entries.toSet()
    val selectedDifficulties: Set<Difficulty> = Difficulty.entries.toSet()
    return dataStoreManager.getProgressiveDifficulty().map { level ->
      ChallengeSettings.challengeProgressionList.filter {
        (it.operator in selectedOperators) && (it.difficulty in selectedDifficulties)
      }[level].generate()
    }
  }
}
