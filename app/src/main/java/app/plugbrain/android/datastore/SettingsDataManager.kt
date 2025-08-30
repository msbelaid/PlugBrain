package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import app.plugbrain.android.datastore.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val BLOCKED_APPS_KEY = stringSetPreferencesKey("blocked_apps")
val BLOCK_INTERVAL_KEY = intPreferencesKey("block_interval")
val MINIMAL_DIFFICULTY_KEY = intPreferencesKey("minimal_difficulty")
val PROGRESSIVE_DIFFICULTY_KEY = intPreferencesKey("progressive_difficulty")
val BLOCK_APPS_TOGGLE_KEY = booleanPreferencesKey("block_apps_toggle")

const val DEFAULT_BLOCK_INTERVAL = 5
const val DEFAULT_MINIMAL_DIFFICULTY = 1

fun DataStore<Preferences>.getUserSettings(): Flow<UserSettings> =
  data.map {
    UserSettings(
      blockInterval = it[BLOCK_INTERVAL_KEY] ?: DEFAULT_BLOCK_INTERVAL,
      distractiveApps = it[BLOCKED_APPS_KEY] ?: emptySet(),
      selectedMinimalDifficulty = it[MINIMAL_DIFFICULTY_KEY] ?: DEFAULT_MINIMAL_DIFFICULTY,
      currentDifficultyLevel = it[PROGRESSIVE_DIFFICULTY_KEY] ?: it[MINIMAL_DIFFICULTY_KEY] ?: DEFAULT_MINIMAL_DIFFICULTY,
    )
  }

suspend fun DataStore<Preferences>.setBlockInterval(interval: Int) {
  edit { prefs -> prefs[BLOCK_INTERVAL_KEY] = interval }
}

suspend fun DataStore<Preferences>.setSelectedMinimalDifficulty(minDifficulty: Int) {
  edit { prefs -> prefs[MINIMAL_DIFFICULTY_KEY] = minDifficulty }
}

suspend fun DataStore<Preferences>.addDistractiveApp(packageName: String) {
  edit { preferences ->
    preferences[BLOCKED_APPS_KEY] = preferences[BLOCKED_APPS_KEY]?.plus(packageName) ?: emptySet()
  }
}

suspend fun DataStore<Preferences>.removeDistractiveApp(packageName: String) {
  edit { preferences ->
    preferences[BLOCKED_APPS_KEY] = preferences[BLOCKED_APPS_KEY]?.minus(packageName) ?: emptySet()
  }
}

// TODO Extract in a different file
fun DataStore<Preferences>.getBlockAppsToggle() = data
  .map { preferences ->
    preferences[BLOCK_APPS_TOGGLE_KEY]
  }

suspend fun DataStore<Preferences>.updateBlockAppsToggle(isBlocked: Boolean) {
  edit { prefs ->
    prefs[BLOCK_APPS_TOGGLE_KEY] = isBlocked
  }
}

fun DataStore<Preferences>.getProgressiveDifficulty() = data
  .map { preferences ->
    preferences[PROGRESSIVE_DIFFICULTY_KEY] ?: preferences[MINIMAL_DIFFICULTY_KEY] ?: DEFAULT_MINIMAL_DIFFICULTY
  }

suspend fun DataStore<Preferences>.decrementProgressiveDifficulty(steps: Int = 1) {
  edit { prefs ->
    val minDifficulty = prefs[MINIMAL_DIFFICULTY_KEY] ?: DEFAULT_MINIMAL_DIFFICULTY
    val current = prefs[PROGRESSIVE_DIFFICULTY_KEY] ?: minDifficulty

    val newValue = (current - steps).coerceAtLeast(minDifficulty)
    prefs[PROGRESSIVE_DIFFICULTY_KEY] = newValue
    if (newValue == minDifficulty) {
      prefs[BLOCK_APPS_TOGGLE_KEY] = true
    }
  }
}

suspend fun DataStore<Preferences>.incrementProgressiveDifficulty(maxDifficulty: Int) {
  edit { prefs ->
    val current = prefs[PROGRESSIVE_DIFFICULTY_KEY] ?: 0
    val newValue = (current + 1).coerceAtMost(maxDifficulty)
    prefs[PROGRESSIVE_DIFFICULTY_KEY] = newValue
  }
}
