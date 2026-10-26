package app.plugbrain.android.challenges

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.datastore.BLOCK_APPS_TOGGLE_KEY
import app.plugbrain.android.datastore.BLOCK_INTERVAL_KEY
import app.plugbrain.android.datastore.MINIMAL_DIFFICULTY_KEY
import app.plugbrain.android.datastore.PROGRESSIVE_DIFFICULTY_KEY
import app.plugbrain.android.datastore.getBlockAppsToggle
import app.plugbrain.android.datastore.getProgressiveDifficulty
import app.plugbrain.android.datastore.getTimestamps
import app.plugbrain.android.datastore.setLastChallengeTime
import app.plugbrain.android.repository.MathChallengeRepository
import app.plugbrain.android.ui.challenges.presentation.ArithChallengeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

private const val ONE_MINUTE = 60_000L
private const val NOW = 1_000 * ONE_MINUTE
private const val BLOCK_INTERVAL = 5
private const val MAX_DIFFICULTY = 5

/** Difficulty goes up with each solved challenge and back down after long breaks. */
@OptIn(ExperimentalCoroutinesApi::class)
class ArithChallengeViewModelTest {
    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var scope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var viewModel: ArithChallengeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val file = File(tempFolder.root, "settings.preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(scope = scope, produceFile = { file })
        val factory = ChallengeFactory((0..MAX_DIFFICULTY).map { level -> { LevelChallenge(level) } })
        viewModel = ArithChallengeViewModel(dataStore, MathChallengeRepository(dataStore, factory)) { NOW }
        runBlocking<Unit> {
            dataStore.edit {
                it[BLOCK_INTERVAL_KEY] = BLOCK_INTERVAL
                it[MINIMAL_DIFFICULTY_KEY] = 0
                it[BLOCK_APPS_TOGGLE_KEY] = true
            }
        }
    }

    @After
    fun tearDown() {
        scope.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun solvingAChallengeRaisesTheDifficultyAndUnblocks() {
        runBlocking {
            setDifficulty(1)
            viewModel.unblockApps()

            // The difficulty is saved last, so everything else is saved once it changes.
            awaitValue(dataStore.getProgressiveDifficulty(), 2)
            assertEquals(false, dataStore.getBlockAppsToggle().first())
            val timestamps = dataStore.getTimestamps().first()
            assertEquals(NOW, timestamps.lastUnblock)
            assertEquals(NOW, timestamps.lastChallenge)
        }
    }

    @Test
    fun aBreakShorterThanTwoIntervalsKeepsTheDifficulty() {
        runBlocking {
            setDifficulty(3)
            dataStore.setLastChallengeTime(NOW - (2 * BLOCK_INTERVAL - 1) * ONE_MINUTE)

            viewModel.decreaseDifficulty()

            assertEquals(3, dataStore.getProgressiveDifficulty().first())
        }
    }

    @Test
    fun eachBreakOfTwoIntervalsLowersTheDifficultyByOne() {
        runBlocking {
            setDifficulty(3)
            dataStore.setLastChallengeTime(NOW - 2 * (2 * BLOCK_INTERVAL) * ONE_MINUTE)

            viewModel.decreaseDifficulty()

            assertEquals(1, dataStore.getProgressiveDifficulty().first())
            assertEquals(NOW, dataStore.getTimestamps().first().lastChallenge)
        }
    }

    @Test
    fun difficultyDoesNotDropBelowTheChosenMinimum() {
        runBlocking {
            dataStore.edit { it[MINIMAL_DIFFICULTY_KEY] = 2 }
            setDifficulty(3)
            dataStore.setLastChallengeTime(NOW - 100 * BLOCK_INTERVAL * ONE_MINUTE)

            viewModel.decreaseDifficulty()

            assertEquals(2, dataStore.getProgressiveDifficulty().first())
        }
    }

    private suspend fun setDifficulty(level: Int) {
        dataStore.edit { it[PROGRESSIVE_DIFFICULTY_KEY] = level }
    }

    // unblockApps saves in the background, so wait for the value instead of reading it once.
    private suspend fun <T> awaitValue(
        flow: Flow<T>,
        expected: T,
    ) {
        withTimeout(5_000) { flow.first { it == expected } }
    }

    private class LevelChallenge(override val difficultyLevel: Int) : Challenge {
        override fun string(): String = "level $difficultyLevel"
    }
}
