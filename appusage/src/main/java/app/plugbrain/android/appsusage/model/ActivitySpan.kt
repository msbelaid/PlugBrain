package app.plugbrain.android.appsusage.model

/** What the user was doing with their phone. */
enum class PhoneActivity {
  /** The screen was off. */
  Away,

  /** One of the apps the user chose to block was in the foreground. */
  DistractingApp,

  /** The screen was on with any other app, or the home or lock screen. */
  OtherApp,
}

/** The user was doing [activity] from [start] to [end], in milliseconds. */
data class ActivitySpan(
  val start: Long,
  val end: Long,
  val activity: PhoneActivity,
)
