package app.plugbrain.android.repository.model

data class ChallengeSettings(
  val operator: Operator = Operator.ADDITION,
  val difficulty: Difficulty = Difficulty.EASY,
  val minimalDifficulty: Int = 0,
  val operationsNumber: Int = 1,
) {
  fun generate(): MathChallenge = operator.generateChallenge(difficulty)

  companion object {
    val challengeProgressionList = listOf(
      ChallengeSettings(Operator.ADDITION, Difficulty.BEGINNER),
      ChallengeSettings(Operator.ADDITION, Difficulty.EASY),
      ChallengeSettings(Operator.SUBTRACTION, Difficulty.BEGINNER),
      ChallengeSettings(Operator.ADDITION, Difficulty.MEDIUM),
      ChallengeSettings(Operator.SUBTRACTION, Difficulty.EASY),
      ChallengeSettings(Operator.MULTIPLICATION, Difficulty.BEGINNER),
      ChallengeSettings(Operator.SUBTRACTION, Difficulty.MEDIUM),
      ChallengeSettings(Operator.MULTIPLICATION, Difficulty.EASY),
      ChallengeSettings(Operator.ADDITION, Difficulty.HARD),
      ChallengeSettings(Operator.MULTIPLICATION, Difficulty.MEDIUM),
      ChallengeSettings(Operator.SUBTRACTION, Difficulty.HARD),
      ChallengeSettings(Operator.ADDITION, Difficulty.EXPERT),
      ChallengeSettings(Operator.SUBTRACTION, Difficulty.EXPERT),
      ChallengeSettings(Operator.MULTIPLICATION, Difficulty.HARD),
      ChallengeSettings(Operator.MULTIPLICATION, Difficulty.EXPERT),
    )
  }
}

enum class Operator(val symbol: String) {
  ADDITION("+") {
    override fun generateChallenge(difficulty: Difficulty): MathChallenge = when (difficulty) {
      Difficulty.BEGINNER -> {
        val range = 1..10
        MathChallenge(range.random(), range.random(), this)
      }

      Difficulty.EASY -> {
        val units1 = (1..9).random()
        val units2 = (0..(9 - units1)).random()
        val tens1 = (1..8).random()
        val tens2 = (1..9 - tens1).random()
        MathChallenge(tens1 * 10 + units1, tens2 * 10 + units2, this)
      }

      Difficulty.MEDIUM -> {
        val units1 = (1..9).random()
        val units2 = ((10 - units1)..9).random()
        val tens1 = (1..9).random()
        val tens2 = (1..9).random()
        MathChallenge(tens1 * 10 + units1, tens2 * 10 + units2, this)
      }

      Difficulty.HARD -> {
        val range = 100..1_000
        MathChallenge(range.random(), range.random(), this)
      }

      Difficulty.EXPERT -> {
        val range = 1_000..10_000
        MathChallenge(range.random(), range.random(), this)
      }
    }
  },
  SUBTRACTION("-") {
    override fun generateChallenge(difficulty: Difficulty): MathChallenge = when (difficulty) {
      Difficulty.BEGINNER -> {
        val num2 = (1..9).random()
        val num1 = (num2..10).random()
        MathChallenge(num1, num2, this)
      }

      Difficulty.EASY -> {
        val units2 = (1..9).random()
        val units1 = (units2..9).random()
        val tens2 = (1..9).random()
        val tens1 = (tens2..9).random()
        MathChallenge(tens1 * 10 + units1, tens2 * 10 + units2, this)
      }

      Difficulty.MEDIUM -> {
        val num2 = (10..99).random()
        val num1 = (num2..100).random()
        MathChallenge(num1, num2, this)
      }

      Difficulty.HARD -> {
        val num2 = (100..999).random()
        val num1 = (num2..1000).random()
        MathChallenge(num1, num2, this)
      }

      Difficulty.EXPERT -> {
        val num2 = (1000..9999).random()
        val num1 = (num2..10000).random()
        MathChallenge(num1, num2, this)
      }
    }
  },
  MULTIPLICATION("x") {
    override fun generateChallenge(difficulty: Difficulty): MathChallenge {
      val ranges = when (difficulty) {
        Difficulty.BEGINNER -> 1..5 to 1..5
        Difficulty.EASY -> 2..10 to 2..10
        Difficulty.MEDIUM -> 10..99 to 2..10
        Difficulty.HARD -> 10..99 to 10..99
        Difficulty.EXPERT -> 100..999 to 10..99
      }
      return MathChallenge(ranges.first.random(), ranges.second.random(), this)
    }
  },
  ;

  abstract fun generateChallenge(difficulty: Difficulty): MathChallenge
}

enum class Difficulty {
  BEGINNER,
  EASY,
  MEDIUM,
  HARD,
  EXPERT,
}
