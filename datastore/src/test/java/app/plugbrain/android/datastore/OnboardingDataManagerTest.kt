package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class OnboardingDataManagerTest {
    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var scope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>

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
    fun `a fresh install has no saved setup`() {
        runBlocking {
            assertFalse(dataStore.hasSavedSetup().first())
        }
    }

    @Test
    fun `a blocked app counts as saved setup`() {
        runBlocking {
            dataStore.addDistractiveApp("com.example.first")

            assertTrue(dataStore.hasSavedSetup().first())
        }
    }

    @Test
    fun `removing every blocked app leaves no saved setup`() {
        runBlocking {
            dataStore.addDistractiveApp("com.example.first")
            dataStore.removeDistractiveApp("com.example.first")

            assertFalse(dataStore.hasSavedSetup().first())
        }
    }

    @Test
    fun `a saved interval counts as saved setup`() {
        runBlocking {
            dataStore.setBlockInterval(2)

            assertTrue(dataStore.hasSavedSetup().first())
        }
    }

    @Test
    fun `a saved difficulty counts as saved setup`() {
        runBlocking {
            dataStore.setSelectedMinimalDifficulty(5)

            assertTrue(dataStore.hasSavedSetup().first())
        }
    }
}
