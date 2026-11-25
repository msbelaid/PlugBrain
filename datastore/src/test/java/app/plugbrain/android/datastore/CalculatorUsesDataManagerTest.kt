package app.plugbrain.android.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class CalculatorUsesDataManagerTest {
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
    fun `each calculator use adds one to the saved count`() {
        runBlocking {
            assertEquals(1, dataStore.addCalculatorUse())
            assertEquals(2, dataStore.addCalculatorUse())
            assertEquals(3, dataStore.addCalculatorUse())
        }
    }
}
