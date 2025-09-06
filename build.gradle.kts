// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint)
}

tasks.register("generateChallenge") {
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
                """.trimIndent()
            )
            println("Generated challenge class: ${file.absolutePath}")
        }

        val diFile = File(outputDir, "app/plugbrain/android/di/ChallengesModule.kt")
        if (!diFile.exists()) {
            throw GradleException("challengesModule.kt not found at ${diFile.absolutePath}")
        }

        val content = diFile.readText()

        val importLine = "import $packageName.$challengeName"
        val updatedImports = if (!content.contains(importLine)) {
            content.replaceFirst("import app.plugbrain.android.challenges.Challenge",
                "import app.plugbrain.android.challenges.Challenge\n$importLine")
        } else content

        val factoryLine = "factory { $challengeName() } bind Challenge::class"
        val updatedFactories = if (!updatedImports.contains(factoryLine)) {
            updatedImports.replace(
                "// New generated challenges will be added here",
                "$factoryLine\n  // New generated challenges will be added here"
            )
        } else updatedImports

        diFile.writeText(updatedFactories)
        println("Updated challengesModule with $challengeName")
    }
}
