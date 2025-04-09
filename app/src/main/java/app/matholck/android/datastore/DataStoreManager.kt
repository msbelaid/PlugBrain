package dz.univ.usto.mathlock.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    private val BLOCKED_APPS = stringSetPreferencesKey("blocked_apps")

    suspend fun blockApp(packageName: String) {
        context.dataStore.edit { preferences ->
            preferences[BLOCKED_APPS] = preferences[BLOCKED_APPS]?.plus(packageName)?: emptySet()
        }
    }

    suspend fun unblockApp(packageName: String) {
        context.dataStore.edit { preferences ->
            preferences[BLOCKED_APPS] = preferences[BLOCKED_APPS]?.minus(packageName)?: emptySet()
        }
    }

    val getBlockedApps: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[BLOCKED_APPS]?: emptySet()
        }
}
