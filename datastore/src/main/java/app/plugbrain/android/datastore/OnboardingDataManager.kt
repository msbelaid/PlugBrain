package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

/** Whether the user finished onboarding, or null when it was never recorded (fresh install or an older version). */
fun DataStore<Preferences>.isOnboardingCompleted(): Flow<Boolean?> = data.map { it[ONBOARDING_COMPLETED_KEY] }

suspend fun DataStore<Preferences>.setOnboardingCompleted() {
  edit { prefs -> prefs[ONBOARDING_COMPLETED_KEY] = true }
}
