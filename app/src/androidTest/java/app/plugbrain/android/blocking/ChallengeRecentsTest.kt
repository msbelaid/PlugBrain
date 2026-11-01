package app.plugbrain.android.blocking

import android.app.ActivityManager
import android.content.Context
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.plugbrain.android.ScreenshotOnFailureRule
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.di.appsUsageModule
import app.plugbrain.android.ui.challenges.ChallengeActivity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * Issue #229: after a solve, the challenge screen stayed in the recents menu. Opening it from there
 * showed a new challenge with nothing blocked, and solving that one raised the difficulty again.
 */
@RunWith(AndroidJUnit4::class)
class ChallengeRecentsTest {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @get:Rule(order = 1)
    val screenshotOnFailure = ScreenshotOnFailureRule()

    private lateinit var driver: BlockingTestDriver

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        FakeAppsUsageStats.reset()
        loadKoinModules(module { single<AppsUsageStats> { FakeAppsUsageStats } })
        driver = BlockingTestDriver(composeRule)
        driver.closeChallenges()
    }

    @After
    fun tearDown() {
        driver.reset()
        loadKoinModules(appsUsageModule)
    }

    @Test
    fun solvedChallengeLeavesNoTaskInRecents() {
        driver.setUpBlocking(difficulty = 0)
        driver.turnOnPlugBrain()

        FakeAppsUsageStats.minutes = 1
        driver.awaitChallenge()
        assertTrue("The open challenge has no task", challengeTasks().isNotEmpty())

        driver.solveChallengeAndUnlock { FakeAppsUsageStats.minutes = 0 }

        assertTrue(
            "The solved challenge is still in recents: ${challengeTasks()}",
            driver.waitFor { challengeTasks().isEmpty() },
        )
        assertTrue("The solve didn't raise the difficulty", driver.awaitDifficulty(1))
        assertEquals("The difficulty went up more than once", 1, driver.difficulty())
    }

    /** PlugBrain's tasks in recents that open the challenge screen when tapped. */
    private fun challengeTasks(): List<String> {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.appTasks
            .mapNotNull { task ->
                // A task removed since the list was read throws here.
                runCatching { task.taskInfo }.getOrNull()
            }.filter { info ->
                info.baseIntent.component?.className == ChallengeActivity::class.java.name
            }.map { it.toString() }
    }
}
