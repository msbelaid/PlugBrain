package app.plugbrain.android.blocking

/**
 * Where usage time comes from. The app answers it from Android's usage stats; tests answer with
 * plain numbers, so the blocking rules never need a device.
 */
fun interface UsageSource {
  /** Minutes [packages] were in the foreground between [from] and [to] (epoch milliseconds). */
  fun minutesUsed(packages: Set<String>, from: Long, to: Long): Long
}
