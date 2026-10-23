package app.plugbrain.android.service

/**
 * Tells when the user opens a distractive app, as opposed to moving between screens of an
 * app already in the foreground. Window events from [ignoredPackages] (PlugBrain's own
 * challenge screen, the system UI, the keyboard) don't change the foreground app, so
 * solving a challenge or pulling down the notification shade doesn't count as a new open.
 */
class AppOpenDetector(private val ignoredPackages: () -> Set<String>) {
  private var foregroundPackage: String? = null

  fun isAppOpening(packageName: String, distractiveApps: Set<String>): Boolean {
    if (packageName in ignoredPackages()) return false
    val previous = foregroundPackage
    foregroundPackage = packageName
    return packageName in distractiveApps && packageName != previous
  }
}

/**
 * An app open is challenged only once [blockIntervalMinutes] have passed since the last solved
 * challenge ([lastUnblock]), so switching between apps right after a solve doesn't loop.
 */
fun isOpenChallengeDue(lastUnblock: Long?, blockIntervalMinutes: Int, now: Long): Boolean =
  lastUnblock == null || now - lastUnblock >= blockIntervalMinutes * 60_000L
