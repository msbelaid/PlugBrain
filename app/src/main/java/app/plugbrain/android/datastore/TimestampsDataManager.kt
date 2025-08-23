package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import app.plugbrain.android.datastore.model.Timestamps
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val LAST_BLOCK_TIME_KEY = longPreferencesKey("last_block_time")
private val LAST_UNBLOCK_TIME_KEY = longPreferencesKey("last_unblock_time")
private val LAST_USAGE_TIME_KEY = longPreferencesKey("last_usage_time")
private val LAST_CHALLENGE_TIME_KEY = longPreferencesKey("last_challenge_time")

private const val DEFAULT_START_TIME = 0L

fun DataStore<Preferences>.getTimestamps(): Flow<Timestamps> =
  data.map {
    Timestamps(
      lastUsage = it[LAST_USAGE_TIME_KEY] ?: DEFAULT_START_TIME,
      lastBlock = it[LAST_BLOCK_TIME_KEY] ?: DEFAULT_START_TIME,
      lastUnblock = it[LAST_UNBLOCK_TIME_KEY] ?: DEFAULT_START_TIME,
      lastChallenge = it[LAST_CHALLENGE_TIME_KEY] ?: DEFAULT_START_TIME,
    )
  }

suspend fun DataStore<Preferences>.setLastUsageTime(millis: Long) =
  setTimestamp(LAST_USAGE_TIME_KEY, millis)

suspend fun DataStore<Preferences>.setLastBlockTime(millis: Long) =
  setTimestamp(LAST_BLOCK_TIME_KEY, millis)

suspend fun DataStore<Preferences>.setLastUnblockTime(millis: Long) =
  setTimestamp(LAST_UNBLOCK_TIME_KEY, millis)

suspend fun DataStore<Preferences>.setLastChallengeTime(millis: Long) =
  setTimestamp(LAST_CHALLENGE_TIME_KEY, millis)

private suspend fun DataStore<Preferences>.setTimestamp(key: Preferences.Key<Long>, millis: Long) {
  edit { prefs -> prefs[key] = millis }
}
