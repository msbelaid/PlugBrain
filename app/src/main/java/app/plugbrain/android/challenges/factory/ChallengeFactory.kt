package app.plugbrain.android.challenges.factory

import app.plugbrain.android.challenges.Challenge

class ChallengeFactory(
  private val challengeProviders: List<Provider<Challenge>>,
) {
  fun getChallengesByDifficulty(difficultyLevel: Int): List<Challenge> {
    return challengeProviders.map { it() }.filter { it.difficultyLevel == difficultyLevel }
  }

  fun getChallengeByDifficulty(difficultyLevel: Int): Challenge {
    return getChallengesByDifficulty(difficultyLevel).random()
  }

  fun maxDifficulty(): Int {
    return challengeProviders.map { it() }.maxOf { it.difficultyLevel }
  }

  fun minDifficulty(): Int {
    return challengeProviders.map { it() }.minOf { it.difficultyLevel }
  }
}
