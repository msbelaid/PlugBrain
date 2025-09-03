package app.plugbrain.android.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.datastore.getProgressiveDifficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MathChallengeRepository(
  private val dataStore: DataStore<Preferences>,
  private val challengesFactory: ChallengeFactory,
) {
  fun generateChallenge(): Flow<Challenge> =
    dataStore.getProgressiveDifficulty().map { level ->
      challengesFactory.getChallengeByDifficulty(level)
    }

  fun getMaxDifficulty(): Int = challengesFactory.maxDifficulty()
}
