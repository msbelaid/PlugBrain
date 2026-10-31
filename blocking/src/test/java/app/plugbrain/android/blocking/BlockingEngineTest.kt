package app.plugbrain.android.blocking

import app.plugbrain.android.blocking.Decision.Ignore
import app.plugbrain.android.blocking.Decision.InUse
import app.plugbrain.android.blocking.Decision.ShowChallenge
import app.plugbrain.android.blocking.UsageDecision.AlreadyBlocked
import app.plugbrain.android.blocking.UsageDecision.Block
import app.plugbrain.android.blocking.UsageDecision.CheckAgainIn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val MINUTE = 60_000L
private const val NOW = 100 * MINUTE
private const val INTERVAL = 5
private const val VIDEO = "com.example.video"
private const val LAUNCHER = "com.example.launcher"
private const val PLUGBRAIN = "app.plugbrain.android"
private const val CALCULATOR = "com.android.calculator2"

class BlockingEngineTest {
    private var usedMinutes = 0L

    private val engine =
        BlockingEngine(
            usageLimit = UsageLimit { _, _, _ -> usedMinutes },
            calculatorPackages = setOf(CALCULATOR),
            ignoredPackages = { setOf(PLUGBRAIN, "com.android.systemui") },
        )

    private fun state(
        isBlocked: Boolean? = false,
        challengeOnAppOpen: Boolean = false,
        lastUnblock: Long? = null,
    ) = BlockingState(setOf(VIDEO), INTERVAL, challengeOnAppOpen, isBlocked, lastBlock = null, lastUnblock = lastUnblock)

    private fun window(
        packageName: String,
        state: BlockingState = state(),
        now: Long = NOW,
    ) = engine.onWindowChanged(packageName, state, now)

    @Test
    fun `nothing happens before the saved state is read`() {
        assertEquals(Ignore, window(VIDEO, BlockingState(blockIntervalMinutes = INTERVAL)))
    }

    @Test
    fun `apps that are not distractive are ignored`() {
        assertEquals(Ignore, window(LAUNCHER))
    }

    @Test
    fun `a blocked app shows the challenge on every window change`() {
        val blocked = state(isBlocked = true)

        assertEquals(ShowChallenge(), window(VIDEO, blocked))
        assertEquals(ShowChallenge(), window(VIDEO, blocked, NOW + 1))
    }

    @Test
    fun `a calculator is covered by the challenge with a warning only while blocked`() {
        assertEquals(ShowChallenge(calculatorWarning = true), window(CALCULATOR, state(isBlocked = true)))
        assertEquals(ShowChallenge(calculatorWarning = true), window("com.vendor.mycalc", state(isBlocked = true)))
        assertEquals(Ignore, window(CALCULATOR, state(isBlocked = false)))
    }

    @Test
    fun `using a distractive app with time left saves the use and checks again later`() {
        usedMinutes = 3

        assertEquals(InUse(at = NOW, usage = CheckAgainIn(2)), window(VIDEO))
    }

    @Test
    fun `using up the interval blocks`() {
        usedMinutes = INTERVAL.toLong()

        assertEquals(InUse(at = NOW, usage = Block(at = NOW)), window(VIDEO))
    }

    @Test
    fun `opening an app is not challenged while the on open switch is off`() {
        assertEquals(InUse(at = NOW, usage = CheckAgainIn(INTERVAL.toLong())), window(VIDEO))
    }

    @Test
    fun `opening an app is challenged when the on open switch is on`() {
        assertEquals(ShowChallenge(), window(VIDEO, state(challengeOnAppOpen = true)))
    }

    @Test
    fun `an open is not challenged again until the interval has passed since the last solve`() {
        val justSolved = state(challengeOnAppOpen = true, lastUnblock = NOW - 2 * MINUTE)
        val solvedLongAgo = state(challengeOnAppOpen = true, lastUnblock = NOW - INTERVAL * MINUTE)

        assertTrue(window(VIDEO, justSolved) is InUse)
        window(LAUNCHER)
        assertEquals(ShowChallenge(), window(VIDEO, solvedLongAgo))
    }

    @Test
    fun `an open challenge left without solving it comes back on the next window change`() {
        val unsolved = state(challengeOnAppOpen = true)
        window(VIDEO, unsolved)

        // Same app again, so not a new open: the challenge is still owed.
        assertEquals(ShowChallenge(), window(VIDEO, unsolved, NOW + 1))
    }

    @Test
    fun `once the open challenge is solved the app can be used`() {
        window(VIDEO, state(challengeOnAppOpen = true))
        val solved = state(challengeOnAppOpen = true, lastUnblock = NOW + 1)

        assertTrue(window(VIDEO, solved, NOW + 2) is InUse)
    }

    @Test
    fun `coming back from the challenge screen is not an open`() {
        window(VIDEO)
        window(PLUGBRAIN)

        // The challenge screen doesn't change the app in front, so the video app isn't opened again.
        assertTrue(window(VIDEO, state(challengeOnAppOpen = true), NOW + 1) is InUse)
    }

    @Test
    fun `a scheduled check does nothing while blocked`() {
        usedMinutes = 99

        assertEquals(AlreadyBlocked, engine.checkUsage(state(isBlocked = true), NOW))
    }

    @Test
    fun `a scheduled check blocks once the interval is used up`() {
        usedMinutes = 6

        assertEquals(Block(at = NOW), engine.checkUsage(state(), NOW))
    }

    @Test
    fun `the challenge is shown again only over a distractive app that still owes one`() {
        assertFalse(engine.shouldShowChallengeAgain(state(isBlocked = true), NOW))

        window(LAUNCHER)
        assertFalse(engine.shouldShowChallengeAgain(state(isBlocked = true), NOW))

        window(VIDEO)
        assertTrue(engine.shouldShowChallengeAgain(state(isBlocked = true), NOW))
        assertFalse(engine.shouldShowChallengeAgain(state(isBlocked = false), NOW))
    }

    @Test
    fun `the challenge is shown again over an unsolved open challenge`() {
        window(VIDEO, state(challengeOnAppOpen = true))

        assertTrue(engine.shouldShowChallengeAgain(state(challengeOnAppOpen = true), NOW + 1))
    }
}
