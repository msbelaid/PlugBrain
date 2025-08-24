package app.plugbrain.android.repository

import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.datastore.DataStoreManager
import app.plugbrain.android.repository.model.ChallengeSettings
import app.plugbrain.android.repository.model.Difficulty
import app.plugbrain.android.repository.model.MathChallenge
import app.plugbrain.android.repository.model.Operator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MathChallengeRepository(
  private val dataStoreManager: DataStoreManager,
  private val challengesFactory: ChallengeFactory,
) {
  fun generateProgressiveChallenge(): Flow<MathChallenge> {
    // TODO pass filtering as parameter, useful if we want to bypass some levels, for example start from medium level, or just use additions.
    val selectedOperators: Set<Operator> = Operator.entries.toSet()
    val selectedDifficulties: Set<Difficulty> = Difficulty.entries.toSet()
    return dataStoreManager.getProgressiveDifficulty().map { level ->
      ChallengeSettings.Companion.challengeProgressionList.filter {
        (it.operator in selectedOperators) && (it.difficulty in selectedDifficulties)
      }[level].generate()
    }
  }

  fun generateChallenge(): Flow<Challenge> =
    dataStoreManager.getProgressiveDifficulty().map { level ->
      challengesFactory.getChallengeByDifficulty(level)
    }
}
