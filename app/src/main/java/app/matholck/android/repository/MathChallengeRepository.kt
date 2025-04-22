package app.matholck.android.repository

import app.matholck.android.repository.model.Difficulty
import app.matholck.android.repository.model.MathChallenge
import app.matholck.android.repository.model.Operator
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MathChallengeRepository(
  private val dataStoreManager: DataStoreManager,
) {
  fun generateChallenge(): Flow<MathChallenge> =
    dataStoreManager.getChallengeSettings().map { challenge ->
      val range = when (challenge.difficulty) {
        Difficulty.BEGINNER -> 1..5
        Difficulty.EASY -> 1..9
        Difficulty.MEDIUM -> 10..99
        Difficulty.HARD -> 100..999
        Difficulty.EXPERT -> 1000..9999
      }
      val num1 = range.random()
      val num2 = range.random()
      MathChallenge(num1, num2, challenge.operator)
    }

  fun generateQuestionAlternative(): Flow<MathChallenge> =
    dataStoreManager.getChallengeSettings().map { challenge ->
      val rangeResult = when (challenge.difficulty) {
        Difficulty.BEGINNER -> 2..10
        Difficulty.EASY -> 10..50
        Difficulty.MEDIUM -> 10..99
        Difficulty.HARD -> 100..999
        Difficulty.EXPERT -> 1000..9999
      }
      when (challenge.operator) {
        Operator.ADDITION -> TODO()
        Operator.SUBTRACTION -> TODO()
        Operator.MULTIPLICATION -> TODO()
      }
      val result = rangeResult.random()
      val num1 = (1..result).random()
      val num2 = result - num1
      MathChallenge(num1, num2, challenge.operator)
    }
}
