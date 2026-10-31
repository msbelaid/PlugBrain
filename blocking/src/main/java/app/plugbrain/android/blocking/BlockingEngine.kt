package app.plugbrain.android.blocking

/**
 * Decides when to challenge the user, from window changes and the usage limit. It knows nothing
 * about Android: the accessibility service feeds it package names, the saved [BlockingState] and
 * the time, then carries out the [Decision]. It keeps only the little memory the rules need: which
 * app is in front, and when the last open challenge was shown.
 *
 * @param calculatorPackages known calculator apps; any package with "calc" in its name counts too.
 * @param ignoredPackages windows that don't change the app in front (PlugBrain, system UI, keyboard).
 */
class BlockingEngine(
  private val usageLimit: UsageLimit,
  private val calculatorPackages: Set<String>,
  ignoredPackages: () -> Set<String>,
) {
  private val appOpenDetector = AppOpenDetector(ignoredPackages)

  // When the last open challenge was shown. Until it's solved, every event of a distractive app
  // brings it back, so leaving it through recents doesn't skip it.
  private var openChallengeShownAt: Long? = null

  fun onWindowChanged(packageName: String, state: BlockingState, now: Long): Decision {
    val isAppOpening = appOpenDetector.isAppOpening(packageName, state.distractiveApps)
    return when {
      packageName in state.distractiveApps -> when {
        state.isBlocked == true -> Decision.ShowChallenge()
        (isAppOpening || isOpenChallengeUnsolved(state)) && shouldChallengeOnOpen(state, now) -> {
          openChallengeShownAt = now
          Decision.ShowChallenge()
        }
        else -> Decision.InUse(at = now, usage = checkUsage(state, now))
      }
      // Showing a warning alone left the calculator usable; cover it with the challenge.
      isCalculator(packageName) && state.isBlocked == true -> Decision.ShowChallenge(calculatorWarning = true)
      else -> Decision.Ignore
    }
  }

  /** Whether the block interval is used up; also called when a scheduled check comes due. */
  fun checkUsage(state: BlockingState, now: Long): UsageDecision {
    if (state.isBlocked == true) return UsageDecision.AlreadyBlocked
    val minutesLeft = usageLimit.minutesLeft(
      state.distractiveApps,
      state.blockIntervalMinutes,
      state.lastBlock,
      state.lastUnblock,
      now,
    )
    return if (minutesLeft <= 0) UsageDecision.Block(at = now) else UsageDecision.CheckAgainIn(minutesLeft)
  }

  /**
   * An app that is still starting up can bring itself back over a challenge that just opened. When
   * the challenge screen turns out to be gone, show it again only if a distractive app is in front
   * and a challenge is still due.
   */
  fun shouldShowChallengeAgain(state: BlockingState, now: Long): Boolean {
    val foreground = appOpenDetector.foregroundPackage ?: return false
    return foreground in state.distractiveApps && isChallengeDue(state, now)
  }

  private fun isChallengeDue(state: BlockingState, now: Long): Boolean =
    state.isBlocked == true || (isOpenChallengeUnsolved(state) && shouldChallengeOnOpen(state, now))

  private fun shouldChallengeOnOpen(state: BlockingState, now: Long): Boolean =
    state.challengeOnAppOpen && isOpenChallengeDue(state.lastUnblock, state.blockIntervalMinutes, now)

  private fun isOpenChallengeUnsolved(state: BlockingState): Boolean {
    val shownAt = openChallengeShownAt ?: return false
    return (state.lastUnblock ?: 0L) < shownAt
  }

  private fun isCalculator(packageName: String): Boolean =
    packageName in calculatorPackages || "calc" in packageName
}
