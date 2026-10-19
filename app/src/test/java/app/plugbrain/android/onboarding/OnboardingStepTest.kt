package app.plugbrain.android.onboarding

import app.plugbrain.android.datastore.model.UserSettings
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.main.presentation.isExistingUser
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
    fun `fresh installs are not existing users`() {
        assertFalse(isExistingUser(UserSettings(), PermissionsState()))
    }

    @Test
    fun `users with blocked apps or granted permissions skip onboarding`() {
        assertTrue(isExistingUser(UserSettings(distractiveApps = setOf("com.youtube")), PermissionsState()))
        assertTrue(
            isExistingUser(
                UserSettings(),
                PermissionsState(
                    accessibilityPermission = true,
                    usageStatsPermission = true,
                    systemAlertWindow = true,
                ),
            ),
        )
    }
}
