package app.plugbrain.android.blocking

/** What the blocking rules read, copied by the app from the user's settings and saved timestamps. */
data class BlockingState(
  val distractiveApps: Set<String> = emptySet(),
  val blockIntervalMinutes: Int,
  /** Also challenge when a distractive app opens, not only after [blockIntervalMinutes] of use. */
  val challengeOnAppOpen: Boolean = false,
  /** Whether the apps are blocked until a challenge is solved; null until the saved value is read. */
  val isBlocked: Boolean? = null,
  val lastBlock: Long? = null,
  /** When the last challenge was solved. */
  val lastUnblock: Long? = null,
  /**
   * When an unsolved challenge was last on screen: now while it still is, null once it's solved or
   * closed. A calculator is only blocked right around this time.
   */
  val challengeOnScreenAt: Long? = null,
)
