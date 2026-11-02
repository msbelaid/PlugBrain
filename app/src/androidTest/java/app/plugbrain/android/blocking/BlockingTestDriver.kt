package app.plugbrain.android.blocking

import android.app.Activity
import android.app.UiAutomation
import android.content.Intent
import android.os.SystemClock
import android.provider.Settings
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ComposeTimeoutException
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
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import androidx.test.uiautomator.Configurator
import androidx.test.uiautomator.UiDevice
import app.plugbrain.android.R
import app.plugbrain.android.datastore.BLOCKED_APPS_KEY
import app.plugbrain.android.datastore.BLOCK_APPS_TOGGLE_KEY
import app.plugbrain.android.datastore.BLOCK_INTERVAL_KEY
import app.plugbrain.android.datastore.CHALLENGE_ON_APP_OPEN_KEY
import app.plugbrain.android.datastore.MINIMAL_DIFFICULTY_KEY
import app.plugbrain.android.datastore.PROGRESSIVE_DIFFICULTY_KEY
import app.plugbrain.android.datastore.getProgressiveDifficulty
import app.plugbrain.android.datastore.setLastBlockTime
import app.plugbrain.android.datastore.setLastChallengeTime
import app.plugbrain.android.datastore.setLastUnblockTime
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

    /**
     * A finished setup that blocks [DISTRACTING_APP] after [blockInterval] minutes of use, and also
     * when it opens if [challengeOnAppOpen] is on. [lastUnblock] is when a challenge was last solved.
     */
    fun setUpBlocking(
        blockInterval: Int = 1,
        difficulty: Int = 0,
        blocked: Boolean = false,
        lastChallenge: Long = System.currentTimeMillis(),
        challengeOnAppOpen: Boolean = false,
        lastUnblock: Long? = null,
    ) {
        runBlocking {
            dataStore.edit {
                it.clear()
                it[BLOCKED_APPS_KEY] = setOf(DISTRACTING_APP)
                it[BLOCK_INTERVAL_KEY] = blockInterval
                it[MINIMAL_DIFFICULTY_KEY] = 0
                it[PROGRESSIVE_DIFFICULTY_KEY] = difficulty
                it[BLOCK_APPS_TOGGLE_KEY] = blocked
                it[CHALLENGE_ON_APP_OPEN_KEY] = challengeOnAppOpen
            }
            // Use from before the test, such as the Settings screens of the onboarding test, doesn't count.
            dataStore.setLastBlockTime(System.currentTimeMillis())
            dataStore.setLastChallengeTime(lastChallenge)
            lastUnblock?.let { dataStore.setLastUnblockTime(it) }
        }
    }

    fun turnOnPlugBrain() {
        permissions.grantOverlay()
        permissions.grantAccessibility()
    }

    fun reset() {
        device.pressHome()
        // Settings plays the distracting app; a later test that opens Settings starts it fresh.
        device.executeShellCommand("am force-stop $DISTRACTING_APP")
        permissions.revokeAll()
        closeChallenges()
        runBlocking { dataStore.edit { it.clear() } }
    }

    /**
     * Finishes challenge screens left open, for example one the service reopened right after a
     * solve. A later test could otherwise see that old screen instead of a new challenge.
     */
    fun closeChallenges() {
        instrumentation.runOnMainSync { openChallenges().forEach { it.finish() } }
        assertTrue("A challenge screen didn't close", waitFor { onMainThread { openChallenges().isEmpty() } })
    }

    private fun openChallenges(): List<Activity> =
        Stage.values()
            .filter { it != Stage.DESTROYED }
            .flatMap { ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(it) }
            .filterIsInstance<ChallengeActivity>()

    private fun <T> onMainThread(block: () -> T): T {
        var result: T? = null
        instrumentation.runOnMainSync { result = block() }
        @Suppress("UNCHECKED_CAST")
        return result as T
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
            openDistractingAppUntil(timeout, relaunchEvery) { ChallengeActivity.isCoveringScreen },
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
        try {
            composeRule.waitUntil(TIMEOUT) {
                try {
                    composeRule.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
                } catch (ignored: IllegalStateException) {
                    // Thrown until the challenge screen has set its content.
                    false
                }
            }
        } catch (e: ComposeTimeoutException) {
            throw AssertionError("Nothing ${matcher.description} after $TIMEOUT ms. On screen: ${textsOnScreen()}", e)
        }
    }

    private fun textsOnScreen(): List<String> =
        try {
            composeRule
                .onAllNodes(SemanticsMatcher("any node") { true }, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .flatMap { node -> node.config.getOrNull(SemanticsProperties.Text).orEmpty().map { it.text } }
        } catch (ignored: IllegalStateException) {
            emptyList()
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
            left == "?" -> missingLeft(operator, right.toInt(), result.toInt())
            right == "?" -> missingRight(left.toInt(), operator, result.toInt())
            else -> compute(left.toInt(), operator, right.toInt())
        }
    }

    /** "? op b = r" */
    private fun missingLeft(
        operator: String,
        b: Int,
        r: Int,
    ): Int =
        when (operator) {
            "+" -> r - b
            "-" -> r + b
            "x" -> r / b
            else -> r * b
        }

    /** "a op ? = r" */
    private fun missingRight(
        a: Int,
        operator: String,
        r: Int,
    ): Int =
        when (operator) {
            "+" -> r - a
            "-" -> a - r
            "x" -> r / a
            else -> a / r
        }

    /** "a op b" */
    private fun compute(
        a: Int,
        operator: String,
        b: Int,
    ): Int =
        when (operator) {
            "+" -> a + b
            "-" -> a - b
            "x" -> a * b
            else -> a / b
        }
}
