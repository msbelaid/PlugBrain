package app.matholck.android.repository

import app.matholck.android.repository.model.MathChallenge
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MathChallengeRepository(
  private val dataStoreManager: DataStoreManager,
) {
  fun generateChallenge(): Flow<MathChallenge> =
    dataStoreManager.getChallengeSettings().map { challenge ->
      challenge.generate()
    }
}
