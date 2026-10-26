package app.plugbrain.android.blocking

import android.os.SystemClock
import android.util.Log
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.di.appsUsageModule
import app.plugbrain.android.ui.challenges.ChallengeActivity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private const val ROUNDS = 15
private const val WATCH_AFTER_SOLVE = 3_000L

/** Temporary: checks whether the challenge comes back right after a solve. */
@RunWith(AndroidJUnit4::class)
class ChallengeReopenReproTest {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    private lateinit var driver: BlockingTestDriver

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
    fun solvingDoesNotBringTheChallengeBack() {
        driver.setUpBlocking(difficulty = 0)
        driver.turnOnPlugBrain()
        val reopenedAfter = mutableListOf<Long>()

        repeat(ROUNDS) { round ->
            FakeAppsUsageStats.minutes = 1
            driver.awaitChallenge()
            driver.solveChallengeAndUnlock { FakeAppsUsageStats.minutes = 0 }
            val solvedAt = SystemClock.uptimeMillis()
            // Wait for the solved screen to go away before watching for a new one.
            driver.waitFor(2_000) { !ChallengeActivity.isInForeground }
            if (driver.waitFor(WATCH_AFTER_SOLVE) { ChallengeActivity.isInForeground }) {
                val after = SystemClock.uptimeMillis() - solvedAt
                Log.w("ChallengeReopenRepro", "Round $round: challenge came back $after ms after the solve")
                reopenedAfter += after
                driver.closeChallenges()
            }
        }

        assertEquals("Rounds where the challenge came back after a solve (ms after): $reopenedAfter", 0, reopenedAfter.size)
    }
}
