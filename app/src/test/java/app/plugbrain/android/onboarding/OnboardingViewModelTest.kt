package app.plugbrain.android.onboarding

import androidx.lifecycle.SavedStateHandle
import app.plugbrain.android.datastore.DEFAULT_BLOCK_INTERVAL
import app.plugbrain.android.datastore.DEFAULT_MINIMAL_DIFFICULTY
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.datastore.hasSavedSetup
import app.plugbrain.android.datastore.model.UserSettings
import app.plugbrain.android.repository.PermissionsRepository
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.testing.ViewModelTestRule
import app.plugbrain.android.testing.awaitMatching
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset
import app.plugbrain.android.ui.onboarding.presentation.OnboardingStep
import app.plugbrain.android.ui.onboarding.presentation.OnboardingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class OnboardingViewModelTest {
    @get:Rule
    val rule = ViewModelTestRule()

    private val granted = PermissionsState(accessibilityPermission = true, usageStatsPermission = true)
    private val savedStateHandle = SavedStateHandle()

    // Stands in for the screen, which keeps collecting the settings while it shows.
    private val screen = CoroutineScope(Dispatchers.Unconfined)

    private val viewModel by lazy {
        rule.cleared(
            OnboardingViewModel(
                permissionsRepository =
                    mock<PermissionsRepository> {
                        on { getPermissions() } doReturn flowOf(granted)
                    },
                dataStore = rule.dataStore,
                savedStateHandle = savedStateHandle,
            ),
        )
    }

    @After
    fun tearDown() {
        screen.cancel()
    }

    @Test
    fun `next walks through every step in order`() {
        assertEquals(OnboardingStep.Welcome, viewModel.step.value)

        viewModel.next()
        assertEquals(OnboardingStep.Permissions, viewModel.step.value)
        viewModel.next()
        assertEquals(OnboardingStep.Apps, viewModel.step.value)
        viewModel.next()
        assertEquals(OnboardingStep.Preferences, viewModel.step.value)
        assertFalse(viewModel.isFinished.value)
    }

    @Test
    fun `back goes to the previous step and leaves from welcome`() {
        viewModel.next()

        assertTrue(viewModel.back())
        assertEquals(OnboardingStep.Welcome, viewModel.step.value)
        assertFalse(viewModel.back())
        assertEquals(OnboardingStep.Welcome, viewModel.step.value)
    }

    @Test
    fun `the step is restored after process death`() {
        savedStateHandle["onboarding_step"] = OnboardingStep.Apps

        assertEquals(OnboardingStep.Apps, viewModel.step.value)
        viewModel.next()
        assertEquals(OnboardingStep.Preferences, savedStateHandle.get<OnboardingStep>("onboarding_step"))
    }

    @Test
    fun `refreshing permissions reads them again`() {
        viewModel.refreshPermissions()

        runBlocking { viewModel.permissions.awaitMatching { it == granted } }
    }

    @Test
    fun `choices are saved as soon as they are made`() {
        // One choice at a time, as the screen makes them.
        viewModel.updateBlockInterval(10)
        assertSaved("The block interval wasn't saved") { it.blockInterval == 10 }
        viewModel.updateDifficulty(DifficultyPreset.Expert)

        assertSaved("The difficulty wasn't saved") {
            it.blockInterval == 10 && it.selectedMinimalDifficulty == DifficultyPreset.Expert.minimalDifficulty
        }
    }

    @Test
    fun `next on the last step saves the defaults shown and finishes`() {
        runBlocking {
            screen.launch { viewModel.settings.collect {} }
            savedStateHandle["onboarding_step"] = OnboardingStep.Preferences

            viewModel.next()

            viewModel.isFinished.awaitMatching { it }
            assertTrue(rule.dataStore.hasSavedSetup().first())
            val settings = rule.dataStore.getUserSettings().first()
            assertEquals(DEFAULT_BLOCK_INTERVAL, settings.blockInterval)
            assertEquals(DEFAULT_MINIMAL_DIFFICULTY, settings.selectedMinimalDifficulty)
        }
    }

    @Test
    fun `finishing keeps the choices made`() {
        runBlocking {
            screen.launch { viewModel.settings.collect {} }
            viewModel.updateBlockInterval(15)
            viewModel.settings.awaitMatching { it.blockInterval == 15 }
            viewModel.updateDifficulty(DifficultyPreset.Intermediate)
            viewModel.settings.awaitMatching { it.blockInterval == 15 && it.selectedMinimalDifficulty == 5 }

            viewModel.completeSetup()

            viewModel.isFinished.awaitMatching { it }
            val settings = rule.dataStore.getUserSettings().first()
            assertEquals(15, settings.blockInterval)
            assertEquals(5, settings.selectedMinimalDifficulty)
        }
    }

    @Test
    fun `skipping finishes without saving anything`() {
        runBlocking {
            viewModel.finish()

            assertTrue(viewModel.isFinished.value)
            assertFalse(rule.dataStore.hasSavedSetup().first())
        }
    }

    /**
     * Reads the saved settings again until they match, so a missed update can't hang the test.
     * Waiting on a single collection of the settings timed out now and then on CI.
     */
    private fun assertSaved(
        message: String,
        predicate: (UserSettings) -> Boolean,
    ) {
        var saved = UserSettings()
        val matched =
            runBlocking {
                withTimeoutOrNull(10_000) {
                    saved = rule.dataStore.getUserSettings().first()
                    while (!predicate(saved)) {
                        delay(50)
                        saved = rule.dataStore.getUserSettings().first()
                    }
                }
            }
        assertTrue("$message, saved: $saved", matched != null)
    }
}
