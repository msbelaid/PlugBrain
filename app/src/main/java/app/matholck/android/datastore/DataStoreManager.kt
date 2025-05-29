package app.matholck.android.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.matholck.android.repository.model.ChallengeSettings
import app.matholck.android.repository.model.ChallengeSettings.Companion.challengeProgressionList
import app.matholck.android.repository.model.Difficulty
import app.matholck.android.repository.model.Operator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DEFAULT_BLOCK_INTERVAL = 15

class DataStoreManager(private val context: Context) {

  private val Context.dataStore by preferencesDataStore(name = "settings")

  private val blockedApps = stringSetPreferencesKey("blocked_apps")
  private val blockInterval = intPreferencesKey("block_interval")
  private val lastBlockTime = longPreferencesKey("last_block_time")
  private val lastUsageTime = longPreferencesKey("last_usage_time")
  private val blockAppsToggle = booleanPreferencesKey("block_apps_toggle")
  private val difficultyLevel = stringPreferencesKey("difficulty_level")
  private val challengeOperator = stringPreferencesKey("operator")
  private val progressiveDifficulty = intPreferencesKey("progressive_difficulty")

  suspend fun blockApp(packageName: String) {
    context.dataStore.edit { preferences ->
      preferences[blockedApps] = preferences[blockedApps]?.plus(packageName) ?: emptySet()
    }
  }

  suspend fun unblockApp(packageName: String) {
    context.dataStore.edit { preferences ->
      preferences[blockedApps] = preferences[blockedApps]?.minus(packageName) ?: emptySet()
    }
  }

  suspend fun updateBlockInterval(minutes: Int) {
    context.dataStore.edit { prefs ->
      prefs[blockInterval] = minutes
    }
  }

  suspend fun updateLastBlockTime(millis: Long) {
    context.dataStore.edit { prefs ->
      prefs[lastBlockTime] = millis
    }
  }

  suspend fun updateLastUsageTime(millis: Long) {
    context.dataStore.edit { prefs ->
      prefs[lastUsageTime] = millis
    }
  }

  suspend fun updateBlockAppsToggle(isBlocked: Boolean) {
    context.dataStore.edit { prefs ->
      prefs[blockAppsToggle] = isBlocked
    }
  }

  suspend fun updateOperator(operator: Operator) {
    context.dataStore.edit { prefs ->
      prefs[challengeOperator] = operator.name
    }
  }

  suspend fun updateDifficulty(difficulty: Difficulty) {
    context.dataStore.edit { prefs ->
      prefs[difficultyLevel] = difficulty.name
    }
  }

  suspend fun updateProgressiveDifficulty(difficultyLevel: Int) {
    context.dataStore.edit { prefs ->
      prefs[progressiveDifficulty] = difficultyLevel
    }
  }

  suspend fun incrementProgressiveDifficulty() {
    context.dataStore.edit { prefs ->
      if ((prefs[progressiveDifficulty] ?: 0) < challengeProgressionList.count() - 1) {
        prefs[progressiveDifficulty] = (prefs[progressiveDifficulty] ?: 0) + 1
      }
    }
  }

  suspend fun decrementProgressiveDifficulty(steps: Int = 1) {
    context.dataStore.edit { prefs ->
      if ((prefs[progressiveDifficulty] ?: 0) > 0) {
        val newValue = (prefs[progressiveDifficulty] ?: 0) - steps
        prefs[progressiveDifficulty] = if (newValue >= 0) newValue else 0
      }
    }
  }

  fun getBlockInterval(): Flow<Int> = context.dataStore.data
    .map { preferences ->
      preferences[blockInterval] ?: DEFAULT_BLOCK_INTERVAL
    }

  fun getBlockedApps(): Flow<Set<String>> = context.dataStore.data
    .map { preferences ->
      preferences[blockedApps] ?: emptySet()
    }

  fun getLastBlockTime(): Flow<Long?> = context.dataStore.data
    .map { preferences ->
      preferences[lastBlockTime]
    }

  fun getLastUsageTime(): Flow<Long?> = context.dataStore.data
    .map { preferences ->
      preferences[lastUsageTime]
    }

  fun getBlockAppsToggle() = context.dataStore.data
    .map { preferences ->
      preferences[blockAppsToggle]
    }

  fun getChallengeSettings(): Flow<ChallengeSettings> = context.dataStore.data
    .map { preferences ->
      ChallengeSettings(
        operator = Operator.valueOf(preferences[challengeOperator] ?: Operator.ADDITION.name),
        difficulty = Difficulty.valueOf(preferences[difficultyLevel] ?: Difficulty.EASY.name),
      )
    }

  fun getProgressiveDifficulty(): Flow<Int> =
    context.dataStore.data
      .map { preferences -> preferences[progressiveDifficulty] ?: 0 }
}
