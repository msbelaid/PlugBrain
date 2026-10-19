package app.plugbrain.android.ui.onboarding.presentation

/** The onboarding steps in order. Welcome isn't counted in the progress bar. */
enum class OnboardingStep {
  Welcome,
  Permissions,
  Apps,
  Preferences,
  ;

  /** Position in the progress bar, from 1 for the first step after Welcome. */
  val progress: Int get() = ordinal

  fun next(): OnboardingStep? = entries.getOrNull(ordinal + 1)

  fun previous(): OnboardingStep? = entries.getOrNull(ordinal - 1)

  companion object {
    val progressSteps: Int = entries.size - 1
  }
}

/** Starting difficulty choices, mapped to the challenges' minimal difficulty level (1 to 15). */
enum class DifficultyPreset(val minimalDifficulty: Int) {
  Beginner(1),
  Intermediate(5),
  MathMaster(10),
  ;

  companion object {
    fun fromMinimalDifficulty(level: Int): DifficultyPreset? = entries.firstOrNull { it.minimalDifficulty == level }
  }
}

/** Block interval choices offered during onboarding, in minutes. */
val OnboardingBlockIntervals = listOf(5, 15, 30)
