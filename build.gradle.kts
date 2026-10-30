import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt)
}

// Merges the unit test coverage of every module into one report: ./gradlew koverXmlReport
dependencies {
    kover(project(":app"))
    kover(project(":appusage"))
    kover(project(":challenges"))
    kover(project(":datastore"))
    kover(project(":designsystem"))

    detektPlugins(libs.compose.rules.detekt)
}

// Merged line coverage may not drop below this floor: koverVerify fails the Unit Tests job.
// It was 66.58% when the floor was set (2026-10-27). Raise it when coverage goes up, never lower it.
kover {
    reports {
        verify {
            rule("Merged line coverage") {
                minBound(66)
            }
        }
    }
}

// Static analysis of every module, tests and buildSrc included, from one task: ./gradlew detekt
// Formatting is ktlint's job, so detekt-formatting isn't used. See config/detekt/detekt.yml.
detekt {
    buildUponDefaultConfig = true
    config.setFrom(file("config/detekt/detekt.yml"))
    source.setFrom(subprojects.map { it.file("src") } + file("buildSrc/src"))
    parallel = true
}

// detekt 1.23 runs on the Kotlin 2.0.21 compiler it was built with, so keep Gradle from
// aligning its Kotlin dependencies to the version the app uses.
configurations.matching { it.name == "detekt" || it.name == "detektPlugins" }.configureEach {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") useVersion("2.0.21")
    }
}

// Keep the ktlint engine the 12.x plugin ran (1.0.1) until the newer rules get their own reformat PR.
allprojects {
    plugins.withId("org.jlleitschuh.gradle.ktlint") {
        configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
            version.set(libs.versions.ktlintEngine.get())
        }
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
    reports {
        html.required.set(true)
        sarif.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
        md.required.set(false)
    }
}

tasks.register<GenerateChallengeTask>("generateChallenge")
