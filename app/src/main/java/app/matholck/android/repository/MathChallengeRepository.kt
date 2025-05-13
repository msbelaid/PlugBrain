package app.matholck.android.repository

import app.matholck.android.repository.model.ChallengeSettings
import app.matholck.android.repository.model.Difficulty
import app.matholck.android.repository.model.MathChallenge
import app.matholck.android.repository.model.Operator
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MathChallengeRepository(
  private val dataStoreManager: DataStoreManager,
) {
  fun generateProgressiveChallenge(): Flow<MathChallenge> {
    // TODO pass filtering as parameter
    val selectedOperators: Set<Operator> = Operator.entries.toSet()
    val selectedDifficulties: Set<Difficulty> = Difficulty.entries.toSet()
    return dataStoreManager.getProgressiveDifficulty().map { level ->
      ChallengeSettings.challengeProgressionList.filter {
        (it.operator in selectedOperators) && (it.difficulty in selectedDifficulties)
      }[level].generate()
    }
  }
}
