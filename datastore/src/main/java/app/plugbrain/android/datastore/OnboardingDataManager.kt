package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Whether the user already saved any part of the setup: apps to block, a block interval or a
 * difficulty. Defaults that were never saved don't count.
 */
fun DataStore<Preferences>.hasSavedSetup(): Flow<Boolean> = data.map {
  !it[BLOCKED_APPS_KEY].isNullOrEmpty() || BLOCK_INTERVAL_KEY in it || MINIMAL_DIFFICULTY_KEY in it
}

/**
 * Saves the default block interval and difficulty, unless the user already chose them. One edit
 * reads and writes the stored values together, so a choice saved just before is never overwritten.
 */
suspend fun DataStore<Preferences>.saveSetupDefaults() {
  edit { prefs ->
    if (BLOCK_INTERVAL_KEY !in prefs) prefs[BLOCK_INTERVAL_KEY] = DEFAULT_BLOCK_INTERVAL
    if (MINIMAL_DIFFICULTY_KEY !in prefs) prefs[MINIMAL_DIFFICULTY_KEY] = DEFAULT_MINIMAL_DIFFICULTY
  }
}
