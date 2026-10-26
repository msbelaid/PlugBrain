package app.plugbrain.android.appsusage

/** Foreground time of apps. Tests replace it to use an app for minutes without waiting. */
interface AppsUsageStats {
  /**
   * @return usage time(in minutes) of all app in filterPackages between startTime and endTime.
   */
  fun getTotalAppsUsageDuration(
    startTime: Long,
    endTime: Long,
    filterPackages: Set<String>,
  ): Long

  /**
   * @return foreground time(in milliseconds) of each app in filterPackages between startTime
   * and endTime. Apps that were not used are missing from the map.
   */
  fun getUsagePerPackage(
    startTime: Long,
    endTime: Long,
    filterPackages: Set<String>,
  ): Map<String, Long>
}
