package app.plugbrain.android.main

import app.plugbrain.android.appsusage.InstalledAppsRepository
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.datastore.addDistractiveApp
import app.plugbrain.android.datastore.setBlockInterval
import app.plugbrain.android.datastore.setLastBlockTime
import app.plugbrain.android.datastore.setSelectedMinimalDifficulty
import app.plugbrain.android.datastore.updateBlockAppsToggle
import app.plugbrain.android.repository.PermissionsRepository
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.testing.RecordingAppsUsageStats
import app.plugbrain.android.testing.ViewModelTestRule
import app.plugbrain.android.testing.awaitMatching
import app.plugbrain.android.testing.levelChallengeFactory
import app.plugbrain.android.ui.main.presentation.DashboardStatus
import app.plugbrain.android.ui.main.presentation.MainScreenState
import app.plugbrain.android.ui.main.presentation.MainScreenViewModel
import app.plugbrain.android.ui.main.presentation.RequiredPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

private const val ONE_HOUR = 60 * 60_000L
private const val MAX_LEVEL = 5

class MainScreenViewModelTest {
    @get:Rule
    val rule = ViewModelTestRule()

    private val instagram = InstalledApp("Instagram", "com.instagram.android")
    private val youtube = InstalledApp("YouTube", "com.google.android.youtube")
    private val calculator = InstalledApp("Calculator", "com.android.calculator2")

    private val allGranted =
        PermissionsState(
            accessibilityPermission = true,
            usageStatsPermission = true,
            batteryOptimizationExemption = true,
            systemAlertWindow = true,
        )

    private val permissions = MutableStateFlow(allGranted)
    private val usage = RecordingAppsUsageStats(usedMinutes = 3)

    private val viewModel by lazy {
        MainScreenViewModel(
            installedAppsRepository = mock<InstalledAppsRepository> {
                on { getInstalledApps() } doReturn flowOf(listOf(calculator, instagram, youtube))
            },
            permissionsRepository = mock<PermissionsRepository> {
                on { getPermissions() } doReturn permissions
            },
            dataStore = rule.dataStore,
            appsUsageStats = usage,
            challengeFactory = levelChallengeFactory(MAX_LEVEL),
        )
    }

    @Test
    fun `shows the blocked apps and the minutes used since the last block`() {
        runBlocking {
            rule.dataStore.addDistractiveApp(instagram.packageName)
            rule.dataStore.addDistractiveApp(youtube.packageName)
            rule.dataStore.setBlockInterval(15)
            rule.dataStore.setLastBlockTime(1_234L)

            val state = refreshAndAwait { it.blockedApps.size == 2 }

            assertEquals(listOf(instagram, youtube), state.blockedApps)
            assertEquals(DashboardStatus.Active(3, 15, 0, MAX_LEVEL), state.status)
            val query = usage.queries.last()
            assertEquals(1_234L, query.startTime)
            assertEquals(setOf(instagram.packageName, youtube.packageName), query.filterPackages)
        }
    }

    @Test
    fun `before the first block, usage is counted over the last hour`() {
        runBlocking {
            rule.dataStore.addDistractiveApp(instagram.packageName)

            refreshAndAwait { it.blockedApps.isNotEmpty() }

            val query = usage.queries.last()
            assertEquals(ONE_HOUR, query.endTime - query.startTime)
        }
    }

    @Test
    fun `no blocked app asks to pick some`() {
        runBlocking {
            val state = refreshAndAwait()

            assertEquals(DashboardStatus.NothingToBlock(5), state.status)
            assertTrue(state.blockedApps.isEmpty())
        }
    }

    @Test
    fun `blocked apps show the current challenge level`() {
        runBlocking {
            rule.dataStore.addDistractiveApp(instagram.packageName)
            rule.dataStore.updateBlockAppsToggle(true)

            val state = refreshAndAwait { it.status is DashboardStatus.AppsLocked }

            assertEquals(DashboardStatus.AppsLocked(awayMinutes = null, challengeLevel = 0), state.status)
        }
    }

    @Test
    fun `a missing permission pauses PlugBrain and battery optimization is flagged`() {
        runBlocking {
            permissions.value = allGranted.copy(usageStatsPermission = false, batteryOptimizationExemption = false)

            val state = refreshAndAwait()

            assertEquals(DashboardStatus.Paused(RequiredPermission.UsageAccess), state.status)
            assertTrue(state.isBatteryOptimized)
        }
    }

    @Test
    fun `granting a permission updates the dashboard`() {
        runBlocking {
            permissions.value = allGranted.copy(accessibilityPermission = false)
            refreshAndAwait { it.status is DashboardStatus.Paused }

            permissions.value = allGranted

            refreshAndAwait { it.status is DashboardStatus.NothingToBlock }
        }
    }

    @Test
    fun `challenge timing is saved and shown`() {
        runBlocking {
            refreshAndAwait()

            viewModel.updateChallengeTiming(interval = 10, challengeOnAppOpen = true)

            val state = viewModel.mainScreenState.awaitMatching { it != null && it.blockInterval == 10 && it.challengeOnAppOpen }
            assertEquals(DashboardStatus.NothingToBlock(10), state?.status)
        }
    }

    @Test
    fun `min difficulty is saved and shown`() {
        runBlocking {
            refreshAndAwait()

            viewModel.updateMinDifficulty(4)

            viewModel.mainScreenState.awaitMatching { it?.minDifficulty == 4 }
        }
    }

    @Test
    fun `sample challenge shows one of the chosen level`() {
        assertEquals("level 2", viewModel.sampleChallenge(2))
        assertNull(viewModel.sampleChallenge(MAX_LEVEL + 1))
    }

    @Test
    fun `onboarding shows on a fresh install`() {
        runBlocking {
            permissions.value = PermissionsState()

            assertTrue(viewModel.needsOnboarding())
        }
    }

    @Test
    fun `onboarding is skipped once part of the setup was saved`() {
        runBlocking {
            permissions.value = PermissionsState()
            rule.dataStore.setSelectedMinimalDifficulty(0)

            assertFalse(viewModel.needsOnboarding())
        }
    }

    @Test
    fun `onboarding is skipped when every required permission is granted`() {
        runBlocking {
            assertFalse(viewModel.needsOnboarding())
        }
    }

    private suspend fun refreshAndAwait(predicate: (MainScreenState) -> Boolean = { true }): MainScreenState {
        viewModel.refresh()
        return checkNotNull(viewModel.mainScreenState.awaitMatching { it != null && predicate(it) })
    }
}
