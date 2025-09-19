import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateChallengeTask : DefaultTask() {

    @get:Input
    val challengeName: String =
        project.findProperty("challengeName") as? String ?: "MyChallenge"

    private val basePackage = "app.plugbrain.android"

    // Currently we only generate challenges with an int number as response
    private val parentClass = "NumericalChallenge"

    private val challengePackage = "$basePackage.challenges"
    private val composePackage = "$basePackage.ui.challenges.compose"
    private val outputDir = File(project.projectDir, "app/src/main/java")


    @get:OutputFile
    val challengeFile: File
        get() = File(outputDir, "${challengePackage.replace(".", "/")}/$challengeName.kt")

    @get:OutputFile
    val uiFile: File
        get() = File(outputDir, "${composePackage.replace(".", "/")}/${challengeName}View.kt")

    @TaskAction
    fun generate() {
        generateChallengeClass()
        generateUiComposable()
        updateNumericalChallengeScreen()
        updateDiModule()
    }

    private fun generateChallengeClass() {
        if (challengeFile.exists()) {
            println("Class $challengeName already exists at ${challengeFile.absolutePath}")
            return
        }

        challengeFile.writeText(
            """
            package $challengePackage

            class $challengeName : $parentClass {
              //TODO("Add the challenge fields")
            
              override fun checkAnswer(response: Int): Boolean {
                TODO("Check the response here!")
              }
            
              override val difficultyLevel: Int
                get() = TODO("Put difficulty level here")
            
              override fun string(): String {
                TODO("How the challenge should be displayed")
              }
            }
            """.trimIndent()
        )
        println("Generated challenge class: ${challengeFile.absolutePath}")
    }

    private fun generateUiComposable() {
        if (uiFile.exists()) {
            println("UI Composable ${challengeName}View already exists at ${uiFile.absolutePath}")
            return
        }

        uiFile.writeText(
            """
            package $composePackage

            import androidx.compose.runtime.Composable
            import androidx.compose.ui.Modifier
            import $challengePackage.$challengeName

            @Composable
            fun ${challengeName}View(
              modifier: Modifier = Modifier,
              challenge: $challengeName,
            ) {
              TODO("Implement UI to display the $challengeName")
            }
            """.trimIndent()
        )
        println("Generated UI Composable: ${uiFile.absolutePath}")
    }

    private fun updateNumericalChallengeScreen() {
        val screenFile =
            File(outputDir, "app/plugbrain/android/ui/challenges/compose/NumericalChallengeScreen.kt")
        if (!screenFile.exists()) {
            throw GradleException("NumericalChallengeScreen.kt not found at ${screenFile.absolutePath}")
        }

        val content = screenFile.readText()

        val updatedImports =
            content.replaceFirst(
                "import $basePackage.challenges.TwoOperandsChallenge",
                "import $basePackage.challenges.TwoOperandsChallenge\nimport $challengePackage.$challengeName"
            )

        val whenCase =
            """    is $challengeName -> ${challengeName}View(
      modifier = Modifier.padding(16.dp),
      challenge = challenge,
    )"""

        val placeholder = "    // Placeholder for generated challenges, do not remove"
        val updatedWhen =
            updatedImports.replace(
                placeholder,
                "$whenCase\n$placeholder"
            )

        screenFile.writeText(updatedWhen)
        println("Updated NumericalChallengeScreen with $challengeName")
    }

    private fun updateDiModule() {
        val diFile = File(outputDir, "app/plugbrain/android/di/ChallengesModule.kt")
        if (!diFile.exists()) {
            throw GradleException("ChallengesModule.kt not found at ${diFile.absolutePath}")
        }

        val content = diFile.readText()

        val importLine = "import $challengePackage.$challengeName"
        val updatedImports =
            content.replaceFirst(
                "import $basePackage.challenges.Challenge",
                "import $basePackage.challenges.Challenge\n$importLine"
            )

        val factoryLine = "  factory { $challengeName() } bind Challenge::class"
        val placeholder = "  // New generated challenges will be added here, do not remove"
        val updatedFactories =
            updatedImports.replace(
                placeholder,
                "$factoryLine\n$placeholder"
            )

        diFile.writeText(updatedFactories)
        println("Updated ChallengesModule with $challengeName")
    }
}
