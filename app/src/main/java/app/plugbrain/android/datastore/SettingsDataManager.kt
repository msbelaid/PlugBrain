package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import app.plugbrain.android.datastore.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val BLOCKED_APPS_KEY = stringSetPreferencesKey("blocked_apps")
val BLOCK_INTERVAL_KEY = intPreferencesKey("block_interval")
val MINIMAL_DIFFICULTY_KEY = intPreferencesKey("minimal_difficulty")

private const val DEFAULT_BLOCK_INTERVAL = 15
private const val DEFAULT_MINIMAL_DIFFICULTY = 1

fun DataStore<Preferences>.getUserSettings(): Flow<Settings> =
  data.map {
    Settings(
      blockInterval = it[BLOCK_INTERVAL_KEY] ?: DEFAULT_BLOCK_INTERVAL,
      distractiveApps = it[BLOCKED_APPS_KEY] ?: emptySet(),
      selectedMinimalDifficulty = it[MINIMAL_DIFFICULTY_KEY] ?: DEFAULT_MINIMAL_DIFFICULTY,
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
