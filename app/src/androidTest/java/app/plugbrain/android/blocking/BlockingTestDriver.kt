package app.plugbrain.android.blocking

import android.app.UiAutomation
import android.content.Intent
import android.os.SystemClock
import android.provider.Settings
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.Configurator
import androidx.test.uiautomator.UiDevice
import app.plugbrain.android.R
import app.plugbrain.android.datastore.BLOCKED_APPS_KEY
import app.plugbrain.android.datastore.BLOCK_APPS_TOGGLE_KEY
import app.plugbrain.android.datastore.BLOCK_INTERVAL_KEY
import app.plugbrain.android.datastore.MINIMAL_DIFFICULTY_KEY
import app.plugbrain.android.datastore.PROGRESSIVE_DIFFICULTY_KEY
import app.plugbrain.android.datastore.getProgressiveDifficulty
import app.plugbrain.android.datastore.setLastBlockTime
import app.plugbrain.android.datastore.setLastChallengeTime
import app.plugbrain.android.onboarding.DevicePermissions
import app.plugbrain.android.ui.challenges.ChallengeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.koin.core.context.GlobalContext

/** The system Settings app plays the distracting app: it's on every emulator and stays open. */
const val DISTRACTING_APP = "com.android.settings"

private const val TIMEOUT = 10_000L
private const val RELAUNCH_EVERY = 3_000L
// Every challenge text: "3 + 1", "3 + ? = 4", "3 + 1 = ?" or "4² = ?".
private val CHALLENGE_TEXT = Regex("""\S+ [-+x/] \S+( = \S+)?|\d+² = \?""")

/**
 * Sets up PlugBrain as after onboarding, opens the distracting app and solves challenges, for the
 * tests of MathLockService and the challenge screen.
 */
class BlockingTestDriver(private val composeRule: ComposeTestRule) {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext
    val dataStore: DataStore<Preferences> get() = GlobalContext.get().get()

    val device: UiDevice
    val permissions: DevicePermissions

    init {
        // Keeps MathLockService running once it's enabled, as on a real phone.
        Configurator.getInstance().uiAutomationFlags = UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES
        device = UiDevice.getInstance(instrumentation)
        permissions = DevicePermissions(device, context.packageName)
    }

    /** A finished setup that blocks [DISTRACTING_APP] after [blockInterval] minutes of use. */
    fun setUpBlocking(
        blockInterval: Int = 1,
        difficulty: Int = 0,
        blocked: Boolean = false,
        lastChallenge: Long = System.currentTimeMillis(),
    ) {
        runBlocking {
            dataStore.edit {
                it.clear()
                it[BLOCKED_APPS_KEY] = setOf(DISTRACTING_APP)
                it[BLOCK_INTERVAL_KEY] = blockInterval
                it[MINIMAL_DIFFICULTY_KEY] = 0
                it[PROGRESSIVE_DIFFICULTY_KEY] = difficulty
                it[BLOCK_APPS_TOGGLE_KEY] = blocked
            }
            // Use from before the test, such as the Settings screens of the onboarding test, doesn't count.
            dataStore.setLastBlockTime(System.currentTimeMillis())
            dataStore.setLastChallengeTime(lastChallenge)
        }
    }

    fun turnOnPlugBrain() {
        permissions.grantOverlay()
        permissions.grantAccessibility()
    }

    fun reset() {
        device.pressHome()
        permissions.revokeAll()
        runBlocking { dataStore.edit { it.clear() } }
    }

    fun openDistractingApp() {
        val intent =
            Intent(Settings.ACTION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    /**
     * Opens the distracting app until [condition] holds. MathLockService connects a moment after
     * accessibility is turned on and only reacts to apps opening, so the first open can go unseen.
     */
    fun openDistractingAppUntil(
        timeout: Long = TIMEOUT,
        relaunchEvery: Long = RELAUNCH_EVERY,
        condition: () -> Boolean,
    ): Boolean {
        val deadline = SystemClock.uptimeMillis() + timeout
        while (SystemClock.uptimeMillis() < deadline) {
            openDistractingApp()
            if (waitFor(minOf(relaunchEvery, deadline - SystemClock.uptimeMillis()), condition)) return true
        }
        return condition()
    }

    fun awaitChallenge(
        timeout: Long = TIMEOUT,
        relaunchEvery: Long = RELAUNCH_EVERY,
    ) {
        assertTrue(
            "The challenge didn't open over the distracting app",
            openDistractingAppUntil(timeout, relaunchEvery) { ChallengeActivity.isInForeground },
        )
    }

    /** Answers the challenge on screen and unlocks the apps, as a user does. */
    fun solveChallengeAndUnlock(
        blockInterval: Int = 1,
        beforeUnlocking: () -> Unit = {},
    ) {
        val question = hasChallengeText()
        waitUntilShown(question)
        val texts = composeRule.onNode(question).fetchSemanticsNode().config[SemanticsProperties.Text]
        val text = texts.first { CHALLENGE_TEXT.matches(it.text) }.text
        composeRule.onNode(hasSetTextAction()).performTextInput(answer(text).toString())
        clickWhenEnabled(context.getString(R.string.challenge_check_answer))
        val unlock = context.getString(R.string.challenge_unlock_app, blockInterval)
        waitUntilShown(hasText(unlock) and hasClickAction() and isEnabled())
        beforeUnlocking()
        clickWhenEnabled(unlock)
    }

    fun difficulty(): Int = runBlocking { dataStore.getProgressiveDifficulty().first() }

    fun awaitDifficulty(expected: Int): Boolean = waitFor(TIMEOUT) { difficulty() == expected }

    fun waitFor(
        timeout: Long = TIMEOUT,
        condition: () -> Boolean,
    ): Boolean {
        val deadline = SystemClock.uptimeMillis() + timeout
        while (SystemClock.uptimeMillis() < deadline) {
            if (condition()) return true
            SystemClock.sleep(200)
        }
        return condition()
    }

    private fun clickWhenEnabled(label: String) {
        val button = hasText(label) and hasClickAction() and isEnabled()
        waitUntilShown(button)
        composeRule.onNode(button).performClick()
    }

    private fun waitUntilShown(matcher: SemanticsMatcher) {
        composeRule.waitUntil(TIMEOUT) {
            try {
                composeRule.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
            } catch (e: IllegalStateException) {
                // Thrown until the challenge screen has set its content.
                false
            }
        }
    }

    private fun hasChallengeText() =
        SemanticsMatcher("shows a challenge like \"3 + 1\"") { node ->
            node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { CHALLENGE_TEXT.matches(it.text) }
        }

    /** Works out the answer from the text, for every kind of challenge the levels can give. */
    private fun answer(challenge: String): Int {
        val tokens = challenge.split(" ")
        if (tokens[0].endsWith("²")) {
            val n = tokens[0].removeSuffix("²").toInt()
            return n * n
        }
        val (left, operator, right) = tokens
        val result = tokens.getOrElse(4) { "?" }
        return when {
            left == "?" -> {
                val b = right.toInt()
                val r = result.toInt()
                when (operator) {
                    "+" -> r - b
                    "-" -> r + b
                    "x" -> r / b
                    else -> r * b
                }
            }
            right == "?" -> {
                val a = left.toInt()
                val r = result.toInt()
                when (operator) {
                    "+" -> r - a
                    "-" -> a - r
                    "x" -> r / a
                    else -> a / r
                }
            }
            else -> {
                val a = left.toInt()
                val b = right.toInt()
                when (operator) {
                    "+" -> a + b
                    "-" -> a - b
                    "x" -> a * b
                    else -> a / b
                }
            }
        }
    }
}
