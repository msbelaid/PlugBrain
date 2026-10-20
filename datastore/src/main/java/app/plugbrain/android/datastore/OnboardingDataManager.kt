package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Whether the user already saved any part of the setup: apps to block, a block interval or a
 * difficulty. Defaults that were never saved don't count.
 */
fun DataStore<Preferences>.hasSavedSetup(): Flow<Boolean> = data.map {
  !it[BLOCKED_APPS_KEY].isNullOrEmpty() || BLOCK_INTERVAL_KEY in it || MINIMAL_DIFFICULTY_KEY in it
}
