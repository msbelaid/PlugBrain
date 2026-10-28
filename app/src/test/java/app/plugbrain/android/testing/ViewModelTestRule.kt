package app.plugbrain.android.testing

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
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
 * folder, so ViewModels read and write real preferences. ViewModels passed to [cleared] are
 * cleared after the test, before the main dispatcher is reset: a viewModelScope still collecting
 * would otherwise touch Dispatchers.Main after the reset and fail a later test.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTestRule : TemporaryFolder() {
    private lateinit var scope: CoroutineScope
    private val viewModels = ViewModelStore()

    lateinit var dataStore: DataStore<Preferences>
        private set

    override fun before() {
        super.before()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val file = File(root, "settings.preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(scope = scope, produceFile = { file })
    }

    /** Returns [viewModel], to be cleared once the test ends. */
    fun <VM : ViewModel> cleared(viewModel: VM): VM {
        val factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel as T
            }
        return ViewModelProvider(viewModels, factory)[viewModel.javaClass]
    }

    override fun after() {
        viewModels.clear()
        scope.cancel()
        Dispatchers.resetMain()
        super.after()
    }
}

/**
 * ViewModels save in the background, so wait for a matching value instead of reading it once. The
 * timeout is generous because the first DataStore read of a test JVM can be slow on CI.
 */
suspend fun <T> Flow<T>.awaitMatching(predicate: (T) -> Boolean): T = withTimeout(10_000) { first(predicate) }
