package dz.univ.usto.mathlock.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DEFAULT_BLOCK_INTERVAL = 15

class DataStoreManager(private val context: Context) {

  private val Context.dataStore by preferencesDataStore(name = "settings")

  private val blockedApps = stringSetPreferencesKey("blocked_apps")
  private val blockInterval = intPreferencesKey("block_interval")
  private val lastBlockTime = longPreferencesKey("last_block_time")

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
      Log.e("Data Store Manager", "Update block interval")
      prefs[blockInterval] = minutes
    }
  }

  suspend fun updateLastBlockTime(millis: Long) {
    context.dataStore.edit { prefs ->
      prefs[lastBlockTime] = millis
    }
  }

  val getBlockInterval: Flow<Int> = context.dataStore.data
    .map { preferences ->
      Log.e("Data Store Manager", "Get block interval" + preferences[blockInterval])
      preferences[blockInterval] ?: DEFAULT_BLOCK_INTERVAL
    }

  val getBlockedApps: Flow<Set<String>> = context.dataStore.data
    .map { preferences ->
      preferences[blockedApps] ?: emptySet()
    }

  val getLastBlockTime: Flow<Long?> = context.dataStore.data
    .map { preferences ->
      preferences[lastBlockTime]
    }
}
