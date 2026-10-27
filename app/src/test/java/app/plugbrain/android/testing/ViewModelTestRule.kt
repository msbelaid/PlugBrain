package app.plugbrain.android.testing

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * Runs viewModelScope on the test thread and gives each test an empty DataStore in a temporary
 * folder, so ViewModels read and write real preferences.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTestRule : TemporaryFolder() {
    private lateinit var scope: CoroutineScope

    lateinit var dataStore: DataStore<Preferences>
        private set

    override fun before() {
        super.before()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val file = File(root, "settings.preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(scope = scope, produceFile = { file })
    }

    override fun after() {
        scope.cancel()
        Dispatchers.resetMain()
        super.after()
    }
}

/** ViewModels save in the background, so wait for a matching value instead of reading it once. */
suspend fun <T> Flow<T>.awaitMatching(predicate: (T) -> Boolean): T = withTimeout(5_000) { first(predicate) }
