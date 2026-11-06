# AGENTS.md

## Commands

```bash
./gradlew assembleDebug            # build a debug APK
./gradlew test                     # unit tests
./gradlew ktlintCheck              # formatting (ktlintFormat fixes most of it)
./gradlew :app:lintRelease         # Android Lint
./gradlew detekt                   # static analysis
./gradlew verifyPaparazziDebug --tests 'app.plugbrain.android.screenshots.*'   # screenshot tests
./gradlew :app:recordPaparazziDebug :designsystem:recordPaparazziDebug          # record screenshots
```

UI tests need an emulator or a device:

```bash
# all UI tests except the smoke test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.notAnnotation=app.plugbrain.android.RealTimeTest

# smoke test only (real usage stats, about 75 s)
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.annotation=app.plugbrain.android.RealTimeTest
```

CI runs the smoke test only on release tags.

## UI

- Build screens from the components in `designsystem`, and use `PlugBrainTheme` colors and typography. Don't add a new component when one already exists.
- Every new screen or component gets screenshot tests in light and dark. Use `snapshotWithLargeFont` for the light one, so it also covers the largest font scale.
- To record new screenshots, run the record command above, or run the "Record snapshots" workflow on your branch. Commit the new images with the change.

## Strings

- Add new strings in English only (`values/strings.xml`). Other languages are translated later.

## New challenge

```bash
./gradlew generateChallenge -PchallengeName=MyNewChallenge
```

Then fill in the generated model. See CONTRIBUTING.md.
