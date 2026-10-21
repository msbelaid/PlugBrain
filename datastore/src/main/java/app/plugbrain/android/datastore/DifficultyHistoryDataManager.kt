package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import app.plugbrain.android.datastore.model.DifficultyChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val DIFFICULTY_HISTORY_KEY = stringPreferencesKey("difficulty_history")

/** The dashboard shows the last 24 hours, so two days of changes are more than enough. */
const val DIFFICULTY_HISTORY_RETENTION = 48 * 60 * 60_000L

/** Every change of the challenge level in the last [DIFFICULTY_HISTORY_RETENTION], oldest first. */
fun DataStore<Preferences>.getDifficultyHistory(): Flow<List<DifficultyChange>> =
  data.map { decodeDifficultyHistory(it[DIFFICULTY_HISTORY_KEY]) }

/** Called from the same edit that changes the level, so the history can't miss a change. */
internal fun MutablePreferences.appendDifficultyChange(change: DifficultyChange) {
  val history = decodeDifficultyHistory(this[DIFFICULTY_HISTORY_KEY]) + change
  this[DIFFICULTY_HISTORY_KEY] = encodeDifficultyHistory(pruneDifficultyHistory(history, now = change.timestamp))
}

/**
 * Drops changes older than [DIFFICULTY_HISTORY_RETENTION], except the last one before that:
 * it gives the level at the start of the kept period.
 */
internal fun pruneDifficultyHistory(history: List<DifficultyChange>, now: Long): List<DifficultyChange> {
  val sorted = history.sortedBy { it.timestamp }
  val firstKept = sorted.indexOfFirst { it.timestamp >= now - DIFFICULTY_HISTORY_RETENTION }
  return when (firstKept) {
    -1 -> sorted.takeLast(1)
    0 -> sorted
    else -> sorted.drop(firstKept - 1)
  }
}

// Stored as "timestamp:level" pairs separated by ";", which keeps the datastore dependency-free.
internal fun encodeDifficultyHistory(history: List<DifficultyChange>): String =
  history.joinToString(";") { "${it.timestamp}:${it.level}" }

internal fun decodeDifficultyHistory(value: String?): List<DifficultyChange> =
  value.orEmpty()
    .split(';')
    .mapNotNull { entry ->
      val (timestamp, level) = entry.split(':').takeIf { it.size == 2 } ?: return@mapNotNull null
      DifficultyChange(timestamp.toLongOrNull() ?: return@mapNotNull null, level.toIntOrNull() ?: return@mapNotNull null)
    }
