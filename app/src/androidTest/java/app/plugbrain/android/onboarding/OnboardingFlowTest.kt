package app.plugbrain.android.onboarding

import android.app.UiAutomation
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.widget.Switch
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Configurator
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import app.plugbrain.android.R
import app.plugbrain.android.datastore.getUserSettings
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import java.util.regex.Pattern

private const val TIMEOUT = 10_000L
private const val SHORT_TIMEOUT = 3_000L
private const val MAX_BACK_PRESSES = 4
private const val SETTINGS_PACKAGE = "com.android.settings"

/**
 * Goes through onboarding like a new user, from the launcher icon to the dashboard, leaving the app
 * for the system settings screens on the way.
 */
@RunWith(AndroidJUnit4::class)
class OnboardingFlowTest {
    // No activity is launched by the rule: the test starts the app from its launcher intent instead.
    @get:Rule
    val composeRule = createEmptyComposeRule()

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext
    private val packageName = context.packageName
    private val dataStore: DataStore<Preferences> get() = GlobalContext.get().get()

    private lateinit var device: UiDevice
    private lateinit var permissions: DevicePermissions

    @Before
    fun setUp() {
        // Keeps MathLockService running once it's enabled, as on a real phone.
        Configurator.getInstance().uiAutomationFlags = UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES
        device = UiDevice.getInstance(instrumentation)
        permissions = DevicePermissions(device, packageName)
        resetToFreshInstall()
    }

    @After
    fun tearDown() {
        device.pressHome()
        resetToFreshInstall()
    }

    /**
     * Permissions are granted with shell commands once their settings screen is open, so the test
     * doesn't depend on how each Android version lays out those screens.
     */
    @Test
    fun newUser_grantsEveryPermission_andLandsOnAnActiveDashboard() {
        launchApp()
        clickWhenEnabled(R.string.welcome_cta_title)

        waitUntilShown(hasText(text(R.string.onboarding_permissions_title)))
        waitUntilShown(hasText(text(R.string.onboarding_permissions_cta)) and isNotEnabled())
        grantInSettings(R.string.onboarding_usage_access_title, R.string.onboarding_sheet_cta, permissions::grantUsageAccess)
        grantInSettings(R.string.onboarding_overlay_title, R.string.onboarding_sheet_cta, permissions::grantOverlay)
        grantInSettings(
            R.string.onboarding_accessibility_title,
            R.string.onboarding_accessibility_accept,
            permissions::grantAccessibility,
        )
        // Battery optimization opens a system dialog straight away, without our sheet.
        grantInSettings(R.string.onboarding_battery_title, sheetCta = null, permissions::grantBatteryExemption)
        clickWhenEnabled(R.string.onboarding_permissions_cta)

        waitUntilShown(isToggleable())
        composeRule.onAllNodes(isToggleable())[0].performClick()
        clickWhenEnabled(R.string.onboarding_apps_cta)

        val intermediate = hasText(text(R.string.onboarding_difficulty_intermediate)) and hasClickAction()
        waitUntilShown(intermediate)
        composeRule.onNode(intermediate).performScrollTo().performClick()
        // The choice is saved in the background; a user sees it selected before tapping Finish.
        waitUntilShown(intermediate and isSelected())
        clickWhenEnabled(R.string.onboarding_preferences_cta)

        waitUntilShown(hasText(text(R.string.dashboard_active)))
        val settings = runBlocking { dataStore.getUserSettings().first() }
        assertEquals(1, settings.distractiveApps.size)
        assertEquals(DifficultyPreset.Intermediate.minimalDifficulty, settings.selectedMinimalDifficulty)
    }

    /** Turns accessibility on the way a user does: find PlugBrain in Settings, switch it on, allow. */
    @Test
    fun accessibility_isTurnedOnInTheSystemSettingsApp() {
        // The Settings selectors below match Android 11, the version CI runs. Other versions name and
        // place things differently.
        assumeTrue(Build.VERSION.SDK_INT == Build.VERSION_CODES.R)
        permissions.grantUsageAccess()
        permissions.grantOverlay()

        launchApp()
        clickWhenEnabled(R.string.welcome_cta_title)
        openSettingsFromRow(R.string.onboarding_accessibility_title, R.string.onboarding_accessibility_accept)

        val appName = text(R.string.app_name)
        val entry =
            device.wait(Until.findObject(By.textContains(appName)), SHORT_TIMEOUT)
                ?: device.findObject(By.scrollable(true))?.scrollUntil(Direction.DOWN, Until.findObject(By.textContains(appName)))
        checkNotNull(entry) { "$appName isn't listed in the accessibility settings" }.click()
        settingsObject(By.clazz(Switch::class.java), "the switch to turn $appName on").click()
        settingsObject(By.text(Pattern.compile("(?i)allow")), "the Allow button").click()

        assertTrue("Accessibility didn't turn on", waitFor { permissions.isAccessibilityEnabled() })
        returnToApp()
        waitUntilGranted(R.string.onboarding_accessibility_title)
        waitUntilShown(hasText(text(R.string.onboarding_permissions_cta)) and isEnabled())
    }

    private fun grantInSettings(
        @StringRes title: Int,
        @StringRes sheetCta: Int?,
        grant: () -> Unit,
    ) {
        openSettingsFromRow(title, sheetCta)
        grant()
        returnToApp()
        waitUntilGranted(title)
    }

    private fun openSettingsFromRow(
        @StringRes title: Int,
        @StringRes sheetCta: Int?,
    ) {
        val row = hasText(text(title)) and hasClickAction()
        waitUntilShown(row)
        composeRule.onNode(row).performScrollTo().performClick()
        if (sheetCta != null) clickWhenEnabled(sheetCta)
        assertTrue(
            "The system settings didn't open for ${text(title)}",
            device.wait(Until.hasObject(By.pkg(SETTINGS_PACKAGE)), TIMEOUT),
        )
    }

    /** Presses Back until PlugBrain is in front again, which also refreshes its permissions. */
    private fun returnToApp() {
        repeat(MAX_BACK_PRESSES) {
            device.pressBack()
            if (device.wait(Until.hasObject(By.pkg(packageName)), SHORT_TIMEOUT)) return
        }
        fail("PlugBrain didn't come back after pressing Back $MAX_BACK_PRESSES times")
    }

    private fun waitUntilGranted(
        @StringRes title: Int,
    ) {
        // A granted permission row is marked as selected.
        waitUntilShown(hasText(text(title)) and isSelected())
    }

    private fun clickWhenEnabled(
        @StringRes label: Int,
    ) {
        val button = hasText(text(label)) and hasClickAction() and isEnabled()
        waitUntilShown(button)
        composeRule.onNode(button).performClick()
    }

    private fun waitUntilShown(matcher: SemanticsMatcher) {
        composeRule.waitUntil(TIMEOUT) {
            try {
                composeRule.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
            } catch (e: IllegalStateException) {
                // Thrown until the app has set its content, while it's still starting up.
                false
            }
        }
    }

    private fun settingsObject(
        selector: BySelector,
        description: String,
    ): UiObject2 =
        device.wait(Until.findObject(selector), TIMEOUT)
            ?: throw AssertionError("Couldn't find $description in the system settings")

    private fun waitFor(condition: () -> Boolean): Boolean {
        val deadline = SystemClock.uptimeMillis() + TIMEOUT
        while (SystemClock.uptimeMillis() < deadline) {
            if (condition()) return true
            SystemClock.sleep(200)
        }
        return condition()
    }

    private fun launchApp() {
        val intent =
            requireNotNull(context.packageManager.getLaunchIntentForPackage(packageName))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    /** No permissions and no saved setup, which is what makes the app show onboarding. */
    private fun resetToFreshInstall() {
        permissions.revokeAll()
        runBlocking { dataStore.edit { it.clear() } }
    }

    private fun text(
        @StringRes id: Int,
    ): String = context.getString(id)
}
