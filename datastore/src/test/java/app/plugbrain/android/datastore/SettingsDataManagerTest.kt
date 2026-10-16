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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class SettingsDataManagerTest {
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
    fun `addDistractiveApp saves the first app on a fresh install`() {
        runBlocking {
            dataStore.addDistractiveApp("com.example.first")

            assertEquals(setOf("com.example.first"), dataStore.getUserSettings().first().distractiveApps)
        }
    }

    @Test
    fun `addDistractiveApp keeps previously added apps`() {
        runBlocking {
            dataStore.addDistractiveApp("com.example.first")
            dataStore.addDistractiveApp("com.example.second")

            assertEquals(
                setOf("com.example.first", "com.example.second"),
                dataStore.getUserSettings().first().distractiveApps,
            )
        }
    }

    @Test
    fun `removeDistractiveApp removes only the given app`() {
        runBlocking {
            dataStore.addDistractiveApp("com.example.first")
            dataStore.addDistractiveApp("com.example.second")

            dataStore.removeDistractiveApp("com.example.first")

            assertEquals(setOf("com.example.second"), dataStore.getUserSettings().first().distractiveApps)
        }
    }

    @Test
    fun `removeDistractiveApp on a fresh install leaves the list empty`() {
        runBlocking {
            dataStore.removeDistractiveApp("com.example.first")

            assertEquals(emptySet<String>(), dataStore.getUserSettings().first().distractiveApps)
        }
    }
}
