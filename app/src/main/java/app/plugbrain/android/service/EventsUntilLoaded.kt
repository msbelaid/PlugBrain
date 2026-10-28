package app.plugbrain.android.service

/**
 * Holds window events back until the service has read the stored settings. Before that the
 * service doesn't know which apps are blocked, so an app opened right after a reboot or a
 * service restart went unchallenged. Only the latest event is kept: it says which app is on top.
 */
class EventsUntilLoaded {
  var isLoaded = false
    private set

  private var pendingPackage: String? = null

  /** True when [packageName] can be handled now; otherwise it's kept for [markLoaded]. */
  fun offer(packageName: String): Boolean {
    if (isLoaded) return true
    pendingPackage = packageName
    return false
  }

  /** Marks the settings as read and returns the event to handle now, if one was held back. */
  fun markLoaded(): String? {
    if (isLoaded) return null
    isLoaded = true
    return pendingPackage.also { pendingPackage = null }
  }
}
