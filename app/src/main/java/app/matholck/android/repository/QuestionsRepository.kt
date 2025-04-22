package app.matholck.android.repository

import app.matholck.android.repository.model.Difficulty
import app.matholck.android.repository.model.Question
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuestionsRepository(
  private val dataStoreManager: DataStoreManager,
) {
  fun generateQuestion(): Flow<Question> =
    dataStoreManager.getChallengeSettings().map { challenge ->
      val range = when (challenge.difficulty) {
        Difficulty.EASY -> 1..9
        Difficulty.MEDIUM -> 10..99
        Difficulty.HARD -> 100..999
      }
      val num1 = range.random()
      val num2 = range.random()
      Question(num1, num2, challenge.operator)
    }
}
