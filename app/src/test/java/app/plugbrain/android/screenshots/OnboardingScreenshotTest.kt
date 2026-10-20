package app.plugbrain.android.screenshots

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.onboarding.AppsStep
import app.plugbrain.android.ui.onboarding.OnboardingPermission
import app.plugbrain.android.ui.onboarding.PermissionExplainerSheet
import app.plugbrain.android.ui.onboarding.PermissionsStep
import app.plugbrain.android.ui.onboarding.PreferencesStep
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset
import app.plugbrain.android.ui.selectapps.presentation.InstalledAppsState
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class OnboardingScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun permissionsStepTest() {
        paparazzi.snapshotWithLargeFont {
            themed { permissionsStep() }
        }
    }

    @Test
    fun permissionsStepDarkTest() {
        paparazzi.snapshot {
            themed(darkTheme = true) { permissionsStep() }
        }
    }

    @Test
    fun permissionsStepAllGrantedTest() {
        paparazzi.snapshot {
            themed {
                PermissionsStep(
                    permissions =
                        PermissionsState(
                            accessibilityPermission = true,
                            usageStatsPermission = true,
                            systemAlertWindow = true,
                        ),
                    onPermissionClick = {},
                    onProceed = {},
                )
            }
        }
    }

    @Test
    fun usageAccessSheetTest() {
        paparazzi.snapshotWithLargeFont {
            themed { explainerSheet(OnboardingPermission.UsageAccess) }
        }
    }

    @Test
    fun accessibilitySheetTest() {
        paparazzi.snapshot {
            themed { explainerSheet(OnboardingPermission.Accessibility) }
        }
    }

    @Test
    fun overlaySheetDarkTest() {
        paparazzi.snapshot {
            themed(darkTheme = true) { explainerSheet(OnboardingPermission.Overlay) }
        }
    }

    @Test
    fun appsStepTest() {
        paparazzi.snapshotWithLargeFont {
            themed { appsStep() }
        }
    }

    @Test
    fun appsStepDarkTest() {
        paparazzi.snapshot {
            themed(darkTheme = true) { appsStep() }
        }
    }

    @Test
    fun preferencesStepTest() {
        paparazzi.snapshotWithLargeFont {
            themed { preferencesStep() }
        }
    }

    @Test
    fun preferencesStepDarkTest() {
        paparazzi.snapshot {
            themed(darkTheme = true) { preferencesStep() }
        }
    }

    @Composable
    private fun themed(
        darkTheme: Boolean = false,
        content: @Composable () -> Unit,
    ) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme, content = content)
    }

    @Composable
    private fun permissionsStep() {
        PermissionsStep(
            permissions = PermissionsState(usageStatsPermission = true),
            onPermissionClick = {},
            onProceed = {},
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun explainerSheet(permission: OnboardingPermission) {
        // The sheet animates in, so a snapshot of its first frame is blank unless it's expanded first.
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.expand() }
        PermissionExplainerSheet(
            permission = permission,
            onUnderstood = {},
            onDismiss = {},
            sheetState = sheetState,
        )
    }

    @Composable
    private fun appsStep() {
        AppsStep(
            installedAppsState =
                InstalledAppsState.Success(
                    apps =
                        listOf(
                            InstalledApp(name = "Subway Surfers", packageName = "com.kiloo.subwaysurf", usageMillis = 104 * 60_000L),
                            InstalledApp(name = "Instagram", packageName = "com.instagram.android", usageMillis = 45 * 60_000L),
                            InstalledApp(name = "Facebook", packageName = "com.facebook.katana", usageMillis = 12 * 60_000L),
                            InstalledApp(name = "Youtube", packageName = "com.google.android.youtube"),
                        ),
                ),
            blockedApps = setOf("com.kiloo.subwaysurf"),
            searchQuery = "",
            onQueryChange = {},
            onAppCheckedChange = { _, _ -> },
            onBlockApps = {},
            onSkip = {},
        )
    }

    @Composable
    private fun preferencesStep() {
        PreferencesStep(
            blockInterval = 5,
            difficulty = DifficultyPreset.Beginner,
            onBlockIntervalSelected = {},
            onDifficultySelected = {},
            onFinish = {},
            onSkip = {},
        )
    }
}
