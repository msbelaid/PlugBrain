package app.plugbrain.android.onboarding

import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.main.presentation.shouldShowOnboarding
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset
import app.plugbrain.android.ui.onboarding.presentation.OnboardingStep
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingStepTest {
    @Test
    fun `steps go forward in order and end after preferences`() {
        assertEquals(OnboardingStep.Permissions, OnboardingStep.Welcome.next())
        assertEquals(OnboardingStep.Apps, OnboardingStep.Permissions.next())
        assertEquals(OnboardingStep.Preferences, OnboardingStep.Apps.next())
        assertNull(OnboardingStep.Preferences.next())
    }

    @Test
    fun `back from welcome has no previous step`() {
        assertNull(OnboardingStep.Welcome.previous())
        assertEquals(OnboardingStep.Apps, OnboardingStep.Preferences.previous())
    }

    @Test
    fun `progress bar counts the steps after welcome`() {
        assertEquals(3, OnboardingStep.progressSteps)
        assertEquals(1, OnboardingStep.Permissions.progress)
        assertEquals(3, OnboardingStep.Preferences.progress)
    }

    @Test
    fun `difficulty presets map to and from the minimal difficulty`() {
        DifficultyPreset.entries.forEach { preset ->
            assertEquals(preset, DifficultyPreset.fromMinimalDifficulty(preset.minimalDifficulty))
        }
        assertNull(DifficultyPreset.fromMinimalDifficulty(3))
    }

    @Test
    fun `onboarding shows when a permission is missing and nothing was saved`() {
        assertTrue(shouldShowOnboarding(PermissionsState(), hasSavedSetup = false))
        assertTrue(shouldShowOnboarding(PermissionsState(usageStatsPermission = true), hasSavedSetup = false))
    }

    @Test
    fun `onboarding is skipped once the setup was saved`() {
        assertFalse(shouldShowOnboarding(PermissionsState(), hasSavedSetup = true))
    }

    @Test
    fun `onboarding is skipped when every required permission is granted`() {
        assertFalse(
            shouldShowOnboarding(
                PermissionsState(
                    accessibilityPermission = true,
                    usageStatsPermission = true,
                    systemAlertWindow = true,
                ),
                hasSavedSetup = false,
            ),
        )
    }

    @Test
    fun `onboarding shows on Xiaomi phones until background pop-ups are allowed`() {
        val granted =
            PermissionsState(
                accessibilityPermission = true,
                usageStatsPermission = true,
                systemAlertWindow = true,
            )
        assertTrue(shouldShowOnboarding(granted.copy(backgroundPopups = false), hasSavedSetup = false))
        assertFalse(shouldShowOnboarding(granted.copy(backgroundPopups = true), hasSavedSetup = false))
    }
}
