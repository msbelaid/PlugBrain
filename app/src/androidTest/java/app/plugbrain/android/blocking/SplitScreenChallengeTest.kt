package app.plugbrain.android.blocking

import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.plugbrain.android.ScreenshotOnFailureRule
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.di.appsUsageModule
import app.plugbrain.android.ui.challenges.ChallengeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.util.concurrent.atomic.AtomicInteger

/**
 * Issue #94: the challenge was put in split screen next to the blocked app, which could then be
 * used. Phones honour resizeableActivity="false"; tablets, foldables and Samsung's "multi window
 * for all apps" don't. The developer option "force activities to be resizable" plays those here.
 */
@RunWith(AndroidJUnit4::class)
class SplitScreenChallengeTest {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @get:Rule(order = 1)
    val screenshotOnFailure = ScreenshotOnFailureRule()

    private lateinit var driver: BlockingTestDriver

    private val uiAutomation = InstrumentationRegistry.getInstrumentation().uiAutomation
    private val scope = CoroutineScope(Dispatchers.Default)
    private val splitScreenExits = AtomicInteger()

    @Before
    fun setUp() {
        FakeAppsUsageStats.reset()
        loadKoinModules(module { single<AppsUsageStats> { FakeAppsUsageStats } })
        driver = BlockingTestDriver(composeRule)
        driver.closeChallenges()
        driver.device.executeShellCommand("settings put global force_resizable_activities 1")
        scope.launch { ChallengeActivity.leftFullScreen.collect { splitScreenExits.incrementAndGet() } }
    }

    @After
    fun tearDown() {
        scope.cancel()
        driver.device.executeShellCommand("settings put global force_resizable_activities 0")
        driver.reset()
        loadKoinModules(appsUsageModule)
    }

    @Test
    fun challengePutInSplitScreen_comesBackFullScreen() {
        driver.setUpBlocking(blocked = true)
        driver.turnOnPlugBrain()
        driver.awaitChallenge()

        assertTrue("Couldn't start split screen", uiAutomation.performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN))

        assertTrue("The challenge never saw it was in split screen", driver.waitFor { splitScreenExits.get() > 0 })
        assertTrue(
            "The challenge didn't come back full screen after split screen",
            driver.waitFor { ChallengeActivity.isCoveringScreen },
        )
    }
}
