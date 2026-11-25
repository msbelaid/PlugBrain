package app.plugbrain.android.blocking

private const val FREE_USES = 2
private const val TOAST_USES = 3

/**
 * How PlugBrain answers a calculator opened during a challenge. It never blocks the calculator
 * (#236): the first uses pass, the next ones show a toast, and from then on a dialog asks whether
 * to go back to the challenge or use the calculator anyway.
 */
enum class CalculatorNudge {
  None,
  Toast,
  Dialog,
  ;

  companion object {
    /** The nudge for the [use]-th calculator use during a challenge, counting from 1 and never reset. */
    fun forUse(use: Int): CalculatorNudge = when {
      use <= FREE_USES -> None
      use <= FREE_USES + TOAST_USES -> Toast
      else -> Dialog
    }
  }
}
