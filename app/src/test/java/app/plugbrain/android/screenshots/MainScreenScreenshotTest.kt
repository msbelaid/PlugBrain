package app.plugbrain.android.screenshots

import android.graphics.drawable.ColorDrawable
import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.appsusage.model.ActivitySpan
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.appsusage.model.PhoneActivity
import app.plugbrain.android.datastore.model.DifficultyChange
import app.plugbrain.android.ui.main.compose.MainScreen
import app.plugbrain.android.ui.main.presentation.DashboardStatus
import app.plugbrain.android.ui.main.presentation.DifficultyTimeline
import app.plugbrain.android.ui.main.presentation.MainScreenState
import app.plugbrain.android.ui.main.presentation.RequiredPermission
import app.plugbrain.android.ui.main.presentation.TIMELINE_DURATION
import app.plugbrain.android.ui.main.presentation.difficultyTimeline
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class MainScreenScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun activeTest() {
        paparazzi.snapshotWithLargeFont {
            mainScreen(status = active)
        }
    }

    @Test
    fun activeDarkTest() {
        paparazzi.snapshot {
            mainScreen(status = active, darkTheme = true)
        }
    }

    @Test
    fun activeWithTimelineTest() {
        paparazzi.snapshotWithLargeFont {
            mainScreen(status = active, timeline = timeline)
        }
    }

    @Test
    fun activeWithTimelineDarkTest() {
        paparazzi.snapshot {
            mainScreen(status = active, darkTheme = true, timeline = timeline)
        }
    }

    @Test
    fun appsLockedTest() {
        paparazzi.snapshotWithLargeFont {
            mainScreen(status = DashboardStatus.AppsLocked(awayMinutes = 6, challengeLevel = 5))
        }
    }

    @Test
    fun pausedTest() {
        paparazzi.snapshotWithLargeFont {
            mainScreen(status = DashboardStatus.Paused(RequiredPermission.Accessibility))
        }
    }

    @Test
    fun pausedDarkTest() {
        paparazzi.snapshot {
            mainScreen(status = DashboardStatus.Paused(RequiredPermission.Accessibility), darkTheme = true)
        }
    }

    @Test
    fun nothingToBlockTest() {
        paparazzi.snapshotWithLargeFont {
            mainScreen(status = DashboardStatus.NothingToBlock(blockInterval = 15), appCount = 0)
        }
    }

    // Plain colors stand in for app icons: Paparazzi can't render the launcher's adaptive icon.
    private val appColors = listOf(0xFFBD100C, 0xFF5B3FB8, 0xFF0C6B78, 0xFFB45309, 0xFF253A3A).map { it.toInt() }

    private val active =
        DashboardStatus.Active(
            usedMinutes = 4,
            blockInterval = 15,
            nextChallengeLevel = 4,
            maxChallengeLevel = 15,
        )

    // A day that climbs during evening scrolling and comes back down overnight.
    private val timeline: DifficultyTimeline by lazy {
        val minute = 60_000L
        // 2026-10-21 09:00 UTC, so the time labels don't depend on when the test runs.
        val now = 1_792_573_200_000L
        val start = now - TIMELINE_DURATION
        val challenges =
            listOf(
                -60 to 2, 74 to 2, 89 to 3, 104 to 4, 314 to 2, 329 to 3, 554 to 2, 569 to 3, 584 to 4, 599 to 5,
                614 to 6, 629 to 7, 644 to 8, 659 to 9, 734 to 8, 749 to 9, 764 to 10, 1424 to 2, 1439 to 3,
            )
        val activity =
            listOf(
                0 to PhoneActivity.OtherApp, 60 to PhoneActivity.DistractingApp, 105 to PhoneActivity.OtherApp,
                180 to PhoneActivity.Away, 240 to PhoneActivity.OtherApp, 300 to PhoneActivity.DistractingApp,
                330 to PhoneActivity.Away, 420 to PhoneActivity.OtherApp, 540 to PhoneActivity.DistractingApp,
                660 to PhoneActivity.OtherApp, 720 to PhoneActivity.DistractingApp, 765 to PhoneActivity.Away,
                1380 to PhoneActivity.OtherApp, 1410 to PhoneActivity.DistractingApp,
            )
        val spanEnds = activity.drop(1).map { it.first } + 1440
        difficultyTimeline(
            history = challenges.map { (minutes, level) -> DifficultyChange(start + minutes * minute, level) },
            storedLevel = 3,
            lastChallenge = null,
            activity =
                activity.zip(spanEnds) { (from, kind), to ->
                    ActivitySpan(start + from * minute, start + to * minute, kind)
                },
            now = now,
            blockInterval = 15,
            minDifficulty = 1,
        )
    }

    @Composable
    private fun mainScreen(
        status: DashboardStatus,
        darkTheme: Boolean = false,
        appCount: Int = 7,
        timeline: DifficultyTimeline? = null,
    ) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            MainScreen(
                state =
                    MainScreenState(
                        status = status,
                        blockedApps =
                            List(appCount) {
                                InstalledApp(
                                    name = "App $it",
                                    packageName = "com.example.app$it",
                                    icon = ColorDrawable(appColors[it % appColors.size]),
                                )
                            },
                        blockInterval = 15,
                        minDifficulty = 1,
                        difficultyTimeline = timeline,
                    ),
                onSettingsClicked = {},
                onAppsClicked = {},
                onTurnBackOn = {},
            )
        }
    }
}
