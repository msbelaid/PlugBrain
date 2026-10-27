package app.plugbrain.android.blocking

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.datastore.getBlockAppsToggle
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

// Long enough that the "no challenge right after a solve" checks can't run into the next interval.
private const val BLOCK_INTERVAL = 5

/**
 * The "Also on app open" switch on the emulator: opening a blocked app is challenged before any
 * use, the challenge can't be skipped by going home, and a solve lets the app open until the
 * block interval has passed. The usage time stays at 0, so only the open can trigger a challenge.
 */
@RunWith(AndroidJUnit4::class)
class AppOpenChallengeTest {
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
    fun openingTheApp_isChallengedBeforeAnyUse_andASolveLetsItOpen() {
        driver.setUpBlocking(blockInterval = BLOCK_INTERVAL, challengeOnAppOpen = true)
        driver.turnOnPlugBrain()

        driver.awaitChallenge()
        // An open challenge doesn't block the apps; only using them for the interval does.
        assertNotEquals(true, runBlocking { driver.dataStore.getBlockAppsToggle().first() })

        driver.solveChallengeAndUnlock(blockInterval = BLOCK_INTERVAL)
        assertTrue("The challenge stayed open after the solve", driver.waitFor { !ChallengeActivity.isInForeground })

        driver.device.pressHome()
        driver.openDistractingApp()
        assertFalse(
            "Opening the app right after a solve was challenged again",
            driver.waitFor(3_000) { ChallengeActivity.isInForeground },
        )
    }

    @Test
    fun goingHomeFromTheChallenge_bringsItBackWhenTheAppOpensAgain() {
        driver.setUpBlocking(blockInterval = BLOCK_INTERVAL, challengeOnAppOpen = true)
        driver.turnOnPlugBrain()
        driver.awaitChallenge()

        driver.device.pressHome()
        assertTrue("The challenge stayed on screen after Home", driver.waitFor { !ChallengeActivity.isInForeground })

        driver.awaitChallenge()
    }

    @Test
    fun aSolveOlderThanTheInterval_challengesTheNextOpen() {
        driver.setUpBlocking(
            blockInterval = 1,
            challengeOnAppOpen = true,
            lastUnblock = System.currentTimeMillis() - 2 * MINUTE,
        )
        driver.turnOnPlugBrain()

        driver.awaitChallenge()
    }

    @Test
    fun withTheSwitchOff_openingTheAppIsNotChallenged() {
        driver.setUpBlocking(blockInterval = BLOCK_INTERVAL, challengeOnAppOpen = false)
        driver.turnOnPlugBrain()

        assertTrue("MathLockService never saw the app open", driver.openDistractingAppUntil { FakeAppsUsageStats.queries > 0 })
        assertFalse("Opening the app was challenged with the switch off", driver.waitFor(3_000) { ChallengeActivity.isInForeground })
    }
}
