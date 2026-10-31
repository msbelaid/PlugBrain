package app.plugbrain.android.blocking

/** What the app should do after a window change, as decided by [BlockingEngine]. */
sealed interface Decision {
  /** Not an app the rules care about. */
  data object Ignore : Decision

  /** Show the challenge now. [calculatorWarning]: a calculator was opened while blocked. */
  data class ShowChallenge(val calculatorWarning: Boolean = false) : Decision

  /** A distractive app is in use and allowed for now: save [at] as its last use, then apply [budget]. */
  data class InUse(val at: Long, val budget: BudgetDecision) : Decision
}

/** What the usage budget of the block interval asks for. */
sealed interface BudgetDecision {
  /** Already blocked: the challenge is shown on the next window change. */
  data object AlreadyBlocked : BudgetDecision

  /** The interval is used up: save the block at [at] and show the challenge. */
  data class Block(val at: Long) : BudgetDecision

  /** Some of the interval is left: check again in [minutes]. */
  data class CheckAgainIn(val minutes: Long) : BudgetDecision
}
