package dz.univ.usto.mathlock.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.matholck.android.repository.model.ChallengeSettings
import app.matholck.android.repository.model.Difficulty
import app.matholck.android.repository.model.Operator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

private const val DEFAULT_BLOCK_INTERVAL = 15

class DataStoreManager(private val context: Context) {

  private val Context.dataStore by preferencesDataStore(name = "settings")

  private val blockedApps = stringSetPreferencesKey("blocked_apps")
  private val blockInterval = intPreferencesKey("block_interval")
  private val lastBlockTime = longPreferencesKey("last_block_time")
  private val blockAppsToggle = booleanPreferencesKey("block_apps_toggle")
  private val difficultyLevel = stringPreferencesKey("difficulty_level")
  private val challengeOperator = stringPreferencesKey("operator")

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

  suspend fun updateBlockAppsToggle(isBlocked: Boolean) {
    context.dataStore.edit { prefs ->
      prefs[blockAppsToggle] = isBlocked
      Timber.e("Write %s", prefs[blockAppsToggle])
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

  fun getBlockAppsToggle(): Flow<Boolean?> {
    Timber.e("DataStoreManager.getBlockAppsToggle called")

    return context.dataStore.data
      .map { preferences ->
        Timber.e("DataStoreManager.getBlockAppsToggle: " + preferences[blockAppsToggle])
        preferences[blockAppsToggle]
      }
  }

  fun getChallengeSettings(): Flow<ChallengeSettings> = context.dataStore.data
    .map { preferences ->
      ChallengeSettings(
        operator = Operator.valueOf(preferences[challengeOperator] ?: Operator.ADDITION.name),
        difficulty = Difficulty.valueOf(preferences[difficultyLevel] ?: Difficulty.EASY.name),
      )
    }
}
