package app.plugbrain.android.blocking

import android.os.SystemClock
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.plugbrain.android.RealTimeTest
import app.plugbrain.android.ScreenshotOnFailureRule
import app.plugbrain.android.datastore.getBlockAppsToggle
import app.plugbrain.android.ui.challenges.ChallengeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// One minute of use, plus up to a minute more when Android counts slightly less than 60 s and
// the service checks again a minute later.
private const val BLOCK_TIMEOUT = 150_000L
private const val RELAUNCH_EVERY = 20_000L
private const val EARLIEST_BLOCK = 55_000L

/**
 * Uses the distracting app for a real minute with the real usage stats, to catch what the fakes in
 * BlockingFlowTest can't: Android's usage events and MathLockService's timer. Takes about
 * 1 to 2 minutes, so it runs only for release tags.
 */
@RealTimeTest
@RunWith(AndroidJUnit4::class)
class BlockingSmokeTest {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @get:Rule(order = 1)
    val screenshotOnFailure = ScreenshotOnFailureRule()

    private lateinit var driver: BlockingTestDriver

    @Before
    fun setUp() {
        driver = BlockingTestDriver(composeRule)
        // The screen must stay on for a whole minute of use.
        driver.device.executeShellCommand("svc power stayon true")
        driver.device.wakeUp()
    }

    @After
    fun tearDown() {
        driver.device.executeShellCommand("svc power stayon false")
        driver.reset()
    }

    @Test
    fun aMinuteOfRealUse_blocksTheApp() {
        driver.setUpBlocking(blockInterval = 1)
        driver.permissions.grantUsageAccess()
        driver.turnOnPlugBrain()

        val start = SystemClock.uptimeMillis()
        // Opening the app again now and then makes sure the service saw it once connected; the
        // usage keeps counting from the setup.
        val blocked = driver.openDistractingAppUntil(BLOCK_TIMEOUT, RELAUNCH_EVERY) { ChallengeActivity.isCoveringScreen }
        val elapsed = SystemClock.uptimeMillis() - start

        assertTrue("Not blocked after ${elapsed / 1000} s of use", blocked)
        assertTrue("Blocked after only ${elapsed / 1000} s of use", elapsed >= EARLIEST_BLOCK)
        assertEquals(true, runBlocking { driver.dataStore.getBlockAppsToggle().first() })
    }
}
