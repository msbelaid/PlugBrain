package app.plugbrain.android.blocking

/**
 * Decides when to challenge the user, from window changes and the usage limit. It knows nothing
 * about Android: the accessibility service feeds it package names, the saved [BlockingState] and
 * the time, then carries out the [Decision]. It keeps only the little memory the rules need: which
 * app is in front, when the last open challenge was shown, and which calculator use was last counted.
 *
 * @param calculatorPackages known calculator apps. Only these count: matching "calc" in any package
 *   name also caught calorie counters, loan calculators and unit converters (#236).
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

  // The challenge visit (its challengeOnScreenAt) and the time of the last counted calculator use.
  private var countedCalculatorVisit: Long? = null
  private var countedCalculatorUseAt: Long? = null

  fun onWindowChanged(packageName: String, state: BlockingState, now: Long): Decision {
    val isAppOpening = appOpenDetector.isAppOpening(packageName, state.distractiveApps)
    return when {
      packageName in state.distractiveApps -> when {
        state.isBlocked == true -> Decision.ShowChallenge
        (isAppOpening || isOpenChallengeUnsolved(state)) && shouldChallengeOnOpen(state, now) -> {
          openChallengeShownAt = now
          Decision.ShowChallenge
        }
        else -> Decision.InUse(at = now, usage = checkUsage(state, now))
      }
      packageName in calculatorPackages && isChallengeOnScreen(state, now) && isNewCalculatorUse(state, now) -> {
        countedCalculatorVisit = state.challengeOnScreenAt
        countedCalculatorUseAt = now
        Decision.CalculatorUsed
      }
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

  /**
   * Whether a challenge is still owed: the apps are blocked, or an open challenge wasn't solved.
   * Used when the challenge screen closes without a solve, such as when it's put in split screen.
   */
  fun isChallengeDue(state: BlockingState, now: Long): Boolean =
    state.isBlocked == true || (isOpenChallengeUnsolved(state) && shouldChallengeOnOpen(state, now))

  private fun shouldChallengeOnOpen(state: BlockingState, now: Long): Boolean =
    state.challengeOnAppOpen && isOpenChallengeDue(state.lastUnblock, state.blockIntervalMinutes, now)

  private fun isOpenChallengeUnsolved(state: BlockingState): Boolean {
    val shownAt = openChallengeShownAt ?: return false
    return (state.lastUnblock ?: 0L) < shownAt
  }

  /**
   * One trip from the challenge to a calculator is one use, however many windows the calculator
   * opens, and coming back to it from PlugBrain's own dialog doesn't count again. A trip is told
   * apart by when the challenge left the screen; a calculator floating over the challenge, which
   * stays on screen, counts at most once per [CHALLENGE_SWITCH_GRACE_MILLIS].
   */
  private fun isNewCalculatorUse(state: BlockingState, now: Long): Boolean {
    if (state.challengeOnScreenAt == countedCalculatorVisit) return false
    val lastUseAt = countedCalculatorUseAt ?: return true
    return now - lastUseAt > CHALLENGE_SWITCH_GRACE_MILLIS
  }

  /**
   * A calculator only helps while a challenge is on screen. Nudging it any other time got in the
   * way of everyday use, such as adding up prices while shopping hours after a block (#236).
   * Switching from the challenge to a calculator goes through recents or Home first, so a challenge
   * that left the screen moments ago still counts.
   */
  private fun isChallengeOnScreen(state: BlockingState, now: Long): Boolean {
    val onScreenAt = state.challengeOnScreenAt ?: return false
    return now - onScreenAt <= CHALLENGE_SWITCH_GRACE_MILLIS && isChallengeDue(state, now)
  }

  companion object {
    /** How long after the challenge leaves the screen a calculator opening still counts as a use. */
    const val CHALLENGE_SWITCH_GRACE_MILLIS = 5_000L
  }
}
