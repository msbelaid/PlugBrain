package app.plugbrain.android.screenshots

import android.graphics.drawable.ColorDrawable
import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.ui.main.compose.MainScreen
import app.plugbrain.android.ui.main.presentation.DashboardStatus
import app.plugbrain.android.ui.main.presentation.MainScreenState
import app.plugbrain.android.ui.main.presentation.RequiredPermission
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

    @Test
    fun batteryOptimizedTest() {
        paparazzi.snapshot {
            mainScreen(status = active, isBatteryOptimized = true)
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

    @Composable
    private fun mainScreen(
        status: DashboardStatus,
        darkTheme: Boolean = false,
        appCount: Int = 7,
        isBatteryOptimized: Boolean = false,
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
                        minDifficulty = 0,
                        isBatteryOptimized = isBatteryOptimized,
                    ),
                onAppsClicked = {},
                onBlockIntervalClicked = {},
                onMinDifficultyClicked = {},
                onBatteryOptimizationClicked = {},
                onTurnBackOn = {},
                onAboutLinkClicked = {},
            )
        }
    }
}
