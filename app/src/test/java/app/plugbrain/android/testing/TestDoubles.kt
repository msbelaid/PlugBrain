package app.plugbrain.android.testing

import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.challenges.factory.ChallengeFactory
import java.util.concurrent.CopyOnWriteArrayList

/** One challenge per level from 0 to [maxLevel], whose text names its level. */
fun levelChallengeFactory(maxLevel: Int) = ChallengeFactory((0..maxLevel).map { level -> { LevelChallenge(level) } })

class LevelChallenge(override val difficultyLevel: Int) : Challenge {
    override fun string(): String = "level $difficultyLevel"
}

/** Returns fixed usage and remembers what was asked, to check the time window and the apps. */
class RecordingAppsUsageStats(
    var usedMinutes: Long = 0,
    var usageByPackage: Map<String, Long> = emptyMap(),
    var failure: Exception? = null,
) : AppsUsageStats {
    // Written from the ViewModel's background thread, read from the test.
    val queries: MutableList<Query> = CopyOnWriteArrayList()

    override fun getTotalAppsUsageDuration(
        startTime: Long,
        endTime: Long,
        filterPackages: Set<String>,
    ): Long {
        queries += Query(startTime, endTime, filterPackages)
        failure?.let { throw it }
        return usedMinutes
    }

    override fun getUsagePerPackage(
        startTime: Long,
        endTime: Long,
        filterPackages: Set<String>,
    ): Map<String, Long> {
        queries += Query(startTime, endTime, filterPackages)
        failure?.let { throw it }
        return usageByPackage.filterKeys { it in filterPackages }
    }

    data class Query(
        val startTime: Long,
        val endTime: Long,
        val filterPackages: Set<String>,
    )
}
