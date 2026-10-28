package app.plugbrain.android.blocking

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.datastore.getBlockAppsToggle
import app.plugbrain.android.datastore.getTimestamps
import app.plugbrain.android.di.appsUsageModule
import app.plugbrain.android.ui.challenges.ChallengeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private const val MINUTE = 60_000L

/**
 * Blocking and difficulty on the emulator, with the real MathLockService and challenge screen.
 * Only the usage time is faked, so reaching the block interval takes seconds instead of minutes.
 * BlockingSmokeTest checks the real usage time for releases.
 */
@RunWith(AndroidJUnit4::class)
class BlockingFlowTest {
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
    fun underTheBlockInterval_theAppStaysOpen() {
        driver.setUpBlocking()
        FakeAppsUsageStats.minutes = 0
        driver.turnOnPlugBrain()

        assertTrue("MathLockService never checked the usage", driver.openDistractingAppUntil { FakeAppsUsageStats.queries > 0 })
        assertFalse("Blocked before the interval was used", driver.waitFor(2_000) { ChallengeActivity.isInForeground })
        assertNotEquals(true, runBlocking { driver.dataStore.getBlockAppsToggle().first() })
    }

    @Test
    fun reachingTheBlockInterval_blocks_andEachSolveRaisesTheDifficulty() {
        driver.setUpBlocking(difficulty = 0)
        FakeAppsUsageStats.minutes = 1
        driver.turnOnPlugBrain()

        driver.awaitChallenge()
        // MathLockService saves the block in the background while the challenge opens.
        assertTrue(
            "Reaching the interval didn't block the apps",
            driver.waitFor { runBlocking { driver.dataStore.getBlockAppsToggle().first() } == true },
        )
        assertTrue(
            "The block time wasn't saved",
            driver.waitFor { runBlocking { driver.dataStore.getTimestamps().first().lastBlock } != null },
        )

        // The usage since this block starts from zero again, as it does with the real usage stats.
        driver.solveChallengeAndUnlock { FakeAppsUsageStats.minutes = 0 }
        assertTrue("Solving didn't raise the difficulty to 1", driver.awaitDifficulty(1))
        assertTrue(
            "Solving didn't unblock the apps",
            driver.waitFor { runBlocking { driver.dataStore.getBlockAppsToggle().first() } == false },
        )

        // Another interval of use blocks again, with a harder challenge.
        FakeAppsUsageStats.minutes = 1
        driver.awaitChallenge()
        driver.solveChallengeAndUnlock { FakeAppsUsageStats.minutes = 0 }
        assertTrue("The second solve didn't raise the difficulty to 2", driver.awaitDifficulty(2))
    }

    @Test
    fun aLongBreak_lowersTheDifficultyWhenTheNextChallengeOpens() {
        // Each 2 block intervals without a challenge take one level off: 5 minutes with a 1 minute
        // interval take off 2.
        driver.setUpBlocking(
            blockInterval = 1,
            difficulty = 4,
            blocked = true,
            lastChallenge = System.currentTimeMillis() - 5 * MINUTE,
        )
        driver.turnOnPlugBrain()

        driver.awaitChallenge()
        assertTrue("The difficulty didn't drop from 4 to 2, it's ${driver.difficulty()}", driver.awaitDifficulty(2))
    }
}
