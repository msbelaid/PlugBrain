package app.plugbrain.android.util

/** The current time in milliseconds. Tests pass a fixed time instead of waiting for minutes to pass. */
fun interface Clock {
  fun now(): Long

  companion object {
    val System = Clock { java.lang.System.currentTimeMillis() }
  }
}
