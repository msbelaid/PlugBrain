package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.plugbrain.android.datastore.model.DifficultyChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class DifficultyHistoryDataManagerTest {
    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var scope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>

    private val hour = 60 * 60_000L

    @Before
    fun setup() {
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val file = File(tempFolder.root, "settings.preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(scope = scope, produceFile = { file })
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    @Test
    fun `history is empty on a fresh install`() {
        runBlocking {
            assertEquals(emptyList<DifficultyChange>(), dataStore.getDifficultyHistory().first())
        }
    }

    @Test
    fun `level changes are logged with their time`() {
        runBlocking {
            dataStore.incrementProgressiveDifficulty(maxDifficulty = 10, now = 1_000)
            dataStore.incrementProgressiveDifficulty(maxDifficulty = 10, now = 2_000)
            dataStore.decrementProgressiveDifficulty(steps = 5, now = 3_000)

            assertEquals(
                listOf(DifficultyChange(1_000, 2), DifficultyChange(2_000, 3), DifficultyChange(3_000, 1)),
                dataStore.getDifficultyHistory().first(),
            )
        }
    }

    @Test
    fun `a change that keeps the level is still logged`() {
        runBlocking {
            dataStore.incrementProgressiveDifficulty(maxDifficulty = 1, now = 1_000)

            assertEquals(listOf(DifficultyChange(1_000, 1)), dataStore.getDifficultyHistory().first())
        }
    }

    @Test
    fun `old changes are dropped except the last one before the kept period`() {
        val now = 100 * hour
        val history =
            listOf(
                DifficultyChange(now - 60 * hour, 2),
                DifficultyChange(now - 50 * hour, 3),
                DifficultyChange(now - 10 * hour, 4),
                DifficultyChange(now, 5),
            )

        assertEquals(history.drop(1), pruneDifficultyHistory(history, now))
    }

    @Test
    fun `only old changes keep the latest one`() {
        val now = 100 * hour
        val history = listOf(DifficultyChange(now - 60 * hour, 2), DifficultyChange(now - 50 * hour, 3))

        assertEquals(history.takeLast(1), pruneDifficultyHistory(history, now))
    }

    @Test
    fun `history survives encoding and skips broken entries`() {
        val history = listOf(DifficultyChange(1_000, 2), DifficultyChange(2_000, 3))

        assertEquals(history, decodeDifficultyHistory(encodeDifficultyHistory(history)))
        assertEquals(history, decodeDifficultyHistory("1000:2;oops;2000:3;3000:"))
        assertEquals(emptyList<DifficultyChange>(), decodeDifficultyHistory(null))
    }
}
