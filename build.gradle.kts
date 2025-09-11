// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint)
}

tasks.register("generateNumberChallenge") {
    val challengeName = project.findProperty("challengeName") as? String ?: "MyChallenge"
    val parentClass = "NumberChallenge"
    val packageName = "app.plugbrain.android.challenges"

    val outputDir = File(project.projectDir, "app/src/main/java")

    doLast {
        val packageDir = File(outputDir, packageName.replace(".", "/"))
        packageDir.mkdirs()

        val file = File(packageDir, "$challengeName.kt")

        if (file.exists()) {
            println("Class $challengeName already exists at ${file.absolutePath}")
        } else {
            file.writeText(
                """
                package $packageName

                class $challengeName : $parentClass {
                  // Put the challenge logic here
                  override fun checkAnswer(response: Int): Boolean {
                    TODO("Check the response here!")
                  }
                
                  override val difficultyLevel: Int
                    get() = TODO("Put difficulty level here")
                
                  override fun string(): String {
                    TODO("How the challenge should be displayed")
                  }
                }
                """.trimIndent(),
            )
            println("Generated challenge class: ${file.absolutePath}")
        }

        val uiPackageName = "app.plugbrain.android.ui.challenges.compose"
        val uiPackageDir = File(outputDir, uiPackageName.replace(".", "/"))
        uiPackageDir.mkdirs()

        val uiFile = File(uiPackageDir, "${challengeName}Screen.kt")

        if (uiFile.exists()) {
            println("UI Composable ${challengeName}Screen already exists at ${uiFile.absolutePath}")
        } else {
            uiFile.writeText(
                """
                package $uiPackageName

                import androidx.compose.runtime.Composable
                import androidx.compose.ui.Modifier
                import $packageName.$challengeName

                @Composable
                fun ${challengeName}Screen(
                  modifier: Modifier = Modifier,
                  challenge: $challengeName,
                  checkAnswer: (Int) -> Unit,
                ) {
                  TODO("Implement UI for $challengeName")
                }
                """.trimIndent(),
            )
            println("Generated UI Composable: ${uiFile.absolutePath}")
        }

        val numberScreenFile =
            File(outputDir, "app/plugbrain/android/ui/challenges/compose/NumberChallengeScreen.kt")
        if (!numberScreenFile.exists()) {
            throw GradleException("NumberChallengeScreen.kt not found at ${numberScreenFile.absolutePath}")
        }

        val screenContent = numberScreenFile.readText()

        val screenImport = "import $uiPackageName.${challengeName}Screen"
        val updatedScreenImports =
            if (!screenContent.contains(screenImport)) {
                screenContent.replaceFirst(
                    "import app.plugbrain.android.challenges.TwoOperandsChallenge",
                    "import app.plugbrain.android.challenges.TwoOperandsChallenge\n$screenImport\nimport $packageName.$challengeName",
                )
            } else {
                screenContent
            }

        val whenCase =
            """    is $challengeName -> ${challengeName}Screen(
      modifier = modifier,
      challenge = challenge,
      checkAnswer = checkAnswer,
    )"""

        val screenPlaceholder = "    // Placeholder for generated challenges, do not remove"
        val updatedWhen =
            if (!updatedScreenImports.contains(whenCase)) {
                updatedScreenImports.replace(
                    screenPlaceholder,
                    "$whenCase\n$screenPlaceholder",
                )
            } else {
                updatedScreenImports
            }

        numberScreenFile.writeText(updatedWhen)
        println("Updated NumberChallengeScreen with $challengeName")
        val diFile = File(outputDir, "app/plugbrain/android/di/ChallengesModule.kt")
        if (!diFile.exists()) {
            throw GradleException("challengesModule.kt not found at ${diFile.absolutePath}")
        }

        val content = diFile.readText()

        val importLine = "import $packageName.$challengeName"
        val updatedImports =
            if (!content.contains(importLine)) {
                content.replaceFirst(
                    "import app.plugbrain.android.challenges.Challenge",
                    "import app.plugbrain.android.challenges.Challenge\n$importLine",
                )
            } else {
                content
            }

        val factoryLine = "  factory { $challengeName() } bind Challenge::class"
        val modulePlaceholder = "  // New generated challenges will be added here, do not remove"
        val updatedFactories =
            if (!updatedImports.contains(factoryLine)) {
                updatedImports.replace(
                    modulePlaceholder,
                    "$factoryLine\n$modulePlaceholder",
                )
            } else {
                updatedImports
            }

        diFile.writeText(updatedFactories)
        println("Updated challengesModule with $challengeName")
    }
}
