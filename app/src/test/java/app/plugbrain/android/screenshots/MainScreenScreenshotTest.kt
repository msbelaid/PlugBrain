package app.plugbrain.android.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.R
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
    fun protectionOnTest() {
        paparazzi.snapshotWithLargeFont {
            mainScreen(status = protectionOn)
        }
    }

    @Test
    fun protectionOnDarkTest() {
        paparazzi.snapshot {
            mainScreen(status = protectionOn, darkTheme = true)
        }
    }

    @Test
    fun appsLockedTest() {
        paparazzi.snapshotWithLargeFont {
            mainScreen(status = DashboardStatus.AppsLocked(awayMinutes = 6, challengeLevel = 5))
        }
    }

    @Test
    fun protectionPausedTest() {
        paparazzi.snapshotWithLargeFont {
            mainScreen(status = DashboardStatus.ProtectionPaused(RequiredPermission.Accessibility))
        }
    }

    @Test
    fun protectionPausedDarkTest() {
        paparazzi.snapshot {
            mainScreen(status = DashboardStatus.ProtectionPaused(RequiredPermission.Accessibility), darkTheme = true)
        }
    }

    @Test
    fun nothingToBlockTest() {
        paparazzi.snapshotWithLargeFont {
            mainScreen(status = DashboardStatus.NothingToBlock(blockInterval = 15), appCount = 0)
        }
    }

    private val protectionOn =
        DashboardStatus.ProtectionOn(
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
    ) {
        val icon = LocalContext.current.getDrawable(R.mipmap.ic_launcher)
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            MainScreen(
                state =
                    MainScreenState(
                        status = status,
                        blockedApps = List(appCount) { InstalledApp(name = "App $it", packageName = "com.example.app$it", icon = icon) },
                        blockInterval = 15,
                        minDifficulty = 1,
                    ),
                onSettingsClicked = {},
                onAppsClicked = {},
            )
        }
    }
}
