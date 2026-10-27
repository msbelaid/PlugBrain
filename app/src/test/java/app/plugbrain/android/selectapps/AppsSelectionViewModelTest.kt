package app.plugbrain.android.selectapps

import app.plugbrain.android.appsusage.InstalledAppsRepository
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.datastore.addDistractiveApp
import app.plugbrain.android.testing.RecordingAppsUsageStats
import app.plugbrain.android.testing.ViewModelTestRule
import app.plugbrain.android.testing.awaitMatching
import app.plugbrain.android.ui.selectapps.presentation.AppsSelectionViewModel
import app.plugbrain.android.ui.selectapps.presentation.InstalledAppsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

private const val ONE_DAY = 24 * 60 * 60_000L

class AppsSelectionViewModelTest {
    @get:Rule
    val rule = ViewModelTestRule()

    private val books = InstalledApp("Books", "com.books")
    private val instagram = InstalledApp("Instagram", "com.instagram.android")
    private val youtube = InstalledApp("YouTube", "com.google.android.youtube")

    private val installedApps = MutableStateFlow(listOf(books, instagram, youtube))
    private val usage =
        RecordingAppsUsageStats(
            usageByPackage = mapOf(youtube.packageName to 5_000L, instagram.packageName to 9_000L),
        )

    private val viewModel by lazy {
        rule.cleared(
            AppsSelectionViewModel(
                installedAppsRepository =
                    mock<InstalledAppsRepository> {
                        on { getInstalledApps() } doReturn installedApps
                    },
                appsUsageStats = usage,
                dataStore = rule.dataStore,
            ),
        )
    }

    @Test
    fun `apps used most in the last 24 hours come first`() {
        runBlocking {
            val state = awaitApps()

            assertEquals(listOf("Instagram", "YouTube", "Books"), state.apps.map { it.name })
            assertEquals(9_000L, state.maxUsageMillis)
            val query = usage.queries.single()
            assertEquals(ONE_DAY, query.endTime - query.startTime)
            assertEquals(setOf(books, instagram, youtube).map { it.packageName }.toSet(), query.filterPackages)
        }
    }

    @Test
    fun `without usage access the list stays alphabetical`() {
        runBlocking {
            usage.failure = SecurityException("No usage access")

            val state = awaitApps()

            assertEquals(listOf("Books", "Instagram", "YouTube"), state.apps.map { it.name })
            assertEquals(0L, state.maxUsageMillis)
        }
    }

    @Test
    fun `search matches the name or the package, ignoring case`() {
        runBlocking {
            viewModel.onChangeQuery("INSTA")
            assertEquals(listOf("Instagram"), awaitApps { it.apps.size == 1 }.apps.map { it.name })

            viewModel.onChangeQuery("com.google")
            val state = awaitApps { it.apps.singleOrNull()?.name == "YouTube" }

            // Bars keep the scale of the most used app, even when it's filtered out.
            assertEquals(9_000L, state.maxUsageMillis)
        }
    }

    @Test
    fun `an app installed while picking goes last instead of reordering the list`() {
        runBlocking {
            awaitApps()
            val tiktok = InstalledApp("TikTok", "com.zhiliaoapp.musically")
            usage.usageByPackage += tiktok.packageName to 60_000L

            installedApps.value = installedApps.value + tiktok

            val state = awaitApps { it.apps.size == 4 }
            assertEquals(listOf("Instagram", "YouTube", "Books", "TikTok"), state.apps.map { it.name })
            assertEquals(1, usage.queries.size)
        }
    }

    @Test
    fun `blocking and unblocking apps is saved`() {
        runBlocking {
            rule.dataStore.addDistractiveApp(books.packageName)
            viewModel.blockedAppsFlow.awaitMatching { it == setOf(books.packageName) }

            viewModel.blockApp(instagram.packageName)
            viewModel.blockedAppsFlow.awaitMatching { it == setOf(books.packageName, instagram.packageName) }

            viewModel.unblockApp(books.packageName)
            viewModel.blockedAppsFlow.awaitMatching { it == setOf(instagram.packageName) }
        }
    }

    private suspend fun awaitApps(predicate: (InstalledAppsState.Success) -> Boolean = { true }): InstalledAppsState.Success =
        viewModel.installedApps.awaitMatching { it is InstalledAppsState.Success && predicate(it) } as InstalledAppsState.Success
}
