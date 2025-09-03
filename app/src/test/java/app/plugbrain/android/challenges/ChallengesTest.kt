package app.plugbrain.android.challenges

import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.di.challengesModule
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class ChallengesTest : KoinTest {
    private val factory: ChallengeFactory by inject()

    @Before
    fun setup() {
        startKoin {
            modules(challengesModule)
        }
    }

    @Test
    fun `test consecutive difficulties`() {
        assertTrue(factory.getChallenges().isConsecutive())
    }
}

private fun List<Challenge>.isConsecutive(): Boolean =
    this.sortedBy { it.difficultyLevel }.zipWithNext().all { (a, b) -> b.difficultyLevel - a.difficultyLevel in 0..1 }
