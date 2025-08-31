<div align="center">
  <img src="fastlane/metadata/android/en-US/images/featureGraphic.png" alt="feature graphic plug brain"/>

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
![GitHub release](https://img.shields.io/github/v/release/msbelaid/PlugBrain)
![Build Status](https://github.com/msbelaid/PlugBrain/actions/workflows/release-build.yml/badge.svg)
[![F-Droid](https://img.shields.io/f-droid/v/app.plugbrain.android?label=Download%20on%20F-Droid&color=green&logo=f-droid)](https://f-droid.org/en/packages/app.plugbrain.android)
[![IzzyOnDroid](https://img.shields.io/badge/IzzyOnDroid-Available-blue?logo=android&logoColor=white)](https://apt.izzysoft.de/fdroid/index/apk/app.plugbrain.android)
![GitHub stars](https://img.shields.io/github/stars/msbelaid/PlugBrain?style=social)
</div>

# PlugBrain
PlugBrain is an app that encourages regular breaks from distracting apps by blocking access at scheduled intervals.
To regain access, you’ll need to solve a math challenge that adjusts in difficulty: 
the more frequently you use the apps, 
the harder the challenges become, but the longer you stay away, the easier they get.

<div align="center">
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1.png" alt="Screenshot 1" width="30%" />
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2.png" alt="Screenshot 2" width="30%" />
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3.png" alt="Screenshot 3" width="30%" />
</div>

## Features
  - Blocks distracting apps
  - Unblock apps by solving math challenges
  - Difficulty increases with frequent use, decreases with focus
  - No tracking, no ads
  - No internet required

## How to use
  - Grant all required permissions
  - Select distracting apps
  - Choose your focus frequency
  - Stay focused ;)

## Download
<div align="center">
  <a href="https://f-droid.org/packages/app.plugbrain.android/">
      <img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="F-Droid" height="75"/>
  </a>
  <a href="https://apt.izzysoft.de/fdroid/index/apk/app.plugbrain.android">
      <img src="https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png" alt="IzzyOnDroid" height="75"/>
  </a>
  <a href="https://github.com/msbelaid/PlugBrain/releases/latest/download/app-release.apk">
      <img src="https://user-images.githubusercontent.com/663460/26973090-f8fdc986-4d14-11e7-995a-e7c5e79ed925.png" alt="Download APK from GitHub" height="75"/>
  </a>
  <a href="https://www.openapk.net/plugbrain/app.plugbrain.android/">
      <img src="https://www.openapk.net/images/openapk-badge.png" alt="Get it on Openapk" height="75"/>
  </a>
</div>

## Contributing

Want to contribute? Follow these quick steps:

1. Fork this repository
2. Clone your fork
3. Create a new branch
4. Make your changes
5. Commit your changes
6. Push to the branch
7. Open a pull request
8. Make sure the CI pass

## Tech stack
PlugBrain is built using modern Android libraries and tools:

- 100% [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose) as UI toolkit
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & Flow for asynchronous.
- [Koin](https://insert-koin.io/) for Dependency injection
- Architecture 
  - MVVM Architecture (Declarative View - ViewModel - Model)
  - Repository pattern
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) – Storage for user preferences
- [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) – ViewModel and lifecycle-aware components
- [Material 3](https://m3.material.io/) – Material Design components
- [Coil](https://coil-kt.github.io/coil/) – Image loading for Android
- [UsageStatsManager](https://developer.android.com/reference/android/app/usage/package-summary) for app usage tracking
- [JUnit](https://junit.org/junit5/) – Unit testing
- [MockK](https://mockk.io/) – Mocking library
- [Timber](https://github.com/JakeWharton/timber) – Logging
- [Ktlint](https://pinterest.github.io/ktlint/) – Kotlin code style checking
- [GitHub Actions](https://github.com/features/actions) – CI/CD for builds, tests, and releases

## License
[GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html)

