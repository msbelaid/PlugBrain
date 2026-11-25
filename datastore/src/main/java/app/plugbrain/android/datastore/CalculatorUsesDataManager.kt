package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey

private val CALCULATOR_USES_KEY = intPreferencesKey("calculator_uses")

/** Counts one more calculator use during a challenge and returns the new count. It's never reset. */
suspend fun DataStore<Preferences>.addCalculatorUse(): Int {
  val updated = edit { prefs -> prefs[CALCULATOR_USES_KEY] = (prefs[CALCULATOR_USES_KEY] ?: 0) + 1 }
  return updated[CALCULATOR_USES_KEY] ?: 0
}
