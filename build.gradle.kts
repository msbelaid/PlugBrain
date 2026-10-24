// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kover)
}

// Merges the unit test coverage of every module into one report: ./gradlew koverXmlReport
dependencies {
    kover(project(":app"))
    kover(project(":appusage"))
    kover(project(":challenges"))
    kover(project(":datastore"))
    kover(project(":designsystem"))
}

tasks.register<GenerateChallengeTask>("generateChallenge")
