package app.plugbrain.android.screenshots

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalInspectionMode
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.Snapshot
import app.cash.paparazzi.SnapshotHandler
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.challenges.addition.AdditionChallenge
import app.plugbrain.android.ui.blockinterval.BlockIntervalSheet
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreen
import app.plugbrain.android.ui.main.compose.MainScreen
import app.plugbrain.android.ui.main.presentation.DashboardStatus
import app.plugbrain.android.ui.main.presentation.MainScreenState
import app.plugbrain.android.ui.mindifficulty.MinDifficultySheet
import app.plugbrain.android.ui.onboarding.AppsStep
import app.plugbrain.android.ui.onboarding.WelcomeScreen
import app.plugbrain.android.ui.selectapps.presentation.InstalledAppsState
import app.plugbrain.android.ui.theme.MathlockAppTheme
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

// The en-US phone screenshots used by F-Droid, relative to the app module, where tests run.
private val STORE_SCREENSHOTS_DIR = File("../fastlane/metadata/android/en-US/images/phoneScreenshots")

/**
 * Renders the store screenshots at full Pixel 5 resolution, straight into fastlane's metadata,
 * named in display order. They only run with STORE_SCREENSHOTS=true (see the "Store screenshots"
 * workflow), so the regular snapshot tests don't record or verify them.
 */
class StoreScreenshots {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            snapshotHandler = StoreScreenshotHandler(STORE_SCREENSHOTS_DIR),
            useDeviceResolution = true,
        )

    @Before
    fun onlyWhenAsked() {
        assumeTrue(System.getenv("STORE_SCREENSHOTS") == "true")
    }

    @Test
    fun welcome() {
        paparazzi.snapshot(name = "1") {
            themed { WelcomeScreen(onGetStarted = {}) }
        }
    }

    @Test
    fun chooseApps() {
        paparazzi.snapshot(name = "2") {
            themed { appsStep() }
        }
    }

    @Test
    fun dashboard() {
        paparazzi.snapshot(name = "3") {
            themed { dashboard() }
        }
    }

    @Test
    fun challenge() {
        paparazzi.snapshot(name = "4") {
            themed {
                NumericalChallengeScreen(
                    challenge = challenge,
                    unlockMinutes = 15,
                    onUnlockApp = {},
                    onExitChallenge = {},
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun challengesSheet() {
        paparazzi.snapshot(name = "5") {
            themed {
                Box {
                    dashboard()
                    // The sheet animates in, so a snapshot of its first frame is blank unless it's expanded first.
                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    LaunchedEffect(Unit) { sheetState.expand() }
                    BlockIntervalSheet(
                        blockInterval = 15,
                        challengeOnAppOpen = true,
                        onSave = { _, _ -> },
                        onDismiss = {},
                        sheetState = sheetState,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun minDifficultySheet() {
        paparazzi.snapshot(name = "6") {
            themed {
                Box {
                    dashboard()
                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    LaunchedEffect(Unit) { sheetState.expand() }
                    MinDifficultySheet(
                        minDifficulty = 5,
                        sampleChallenge = { "27 + 48" },
                        onSave = {},
                        onDismiss = {},
                        sheetState = sheetState,
                    )
                }
            }
        }
    }

    @Test
    fun dashboardDark() {
        paparazzi.snapshot(name = "7") {
            themed(darkTheme = true) { dashboard() }
        }
    }

    // Real challenges pick random operands.
    private val challenge =
        object : AdditionChallenge {
            override val operand1 = 25
            override val operand2 = 17
            override val difficultyLevel = 1
        }

    private val apps =
        listOf(
            StoreApp("Instagram", 0xFFC13584, 104),
            StoreApp("TikTok", 0xFF253A3A, 72),
            StoreApp("YouTube", 0xFFBD100C, 45),
            StoreApp("Reddit", 0xFFB45309, 21),
            StoreApp("Snapchat", 0xFF5B3FB8, 12),
            StoreApp("Facebook", 0xFF1D4ED8, 8),
            StoreApp("Chess", 0xFF0C6B78, 0),
        )

    private val blockedPackages = apps.take(5).map { it.packageName }.toSet()

    @Composable
    private fun dashboard() {
        MainScreen(
            state =
                MainScreenState(
                    status =
                        DashboardStatus.Active(
                            usedMinutes = 4,
                            blockInterval = 15,
                            nextChallengeLevel = 4,
                            maxChallengeLevel = 15,
                        ),
                    blockedApps = apps.filter { it.packageName in blockedPackages }.map { it.toInstalledApp() },
                    blockInterval = 15,
                    minDifficulty = 5,
                    isBatteryOptimized = false,
                ),
            onAppsClicked = {},
            onBlockIntervalClicked = {},
            onMinDifficultyClicked = {},
            onBatteryOptimizationClicked = {},
            onTurnBackOn = {},
        )
    }

    @Composable
    private fun appsStep() {
        AppsStep(
            installedAppsState = InstalledAppsState.Success(apps = apps.map { it.toInstalledApp() }),
            blockedApps = blockedPackages,
            searchQuery = "",
            onQueryChange = {},
            onAppCheckedChange = { _, _ -> },
            onBlockApps = {},
            onSkip = {},
        )
    }

    // Inspection mode skips entrance animations; the preview handler hands Coil's painter the icon
    // straight away instead of loading it asynchronously after the snapshot is taken.
    @OptIn(ExperimentalCoilApi::class)
    @Composable
    private fun themed(
        darkTheme: Boolean = false,
        content: @Composable () -> Unit,
    ) {
        CompositionLocalProvider(
            LocalInspectionMode provides true,
            LocalAsyncImagePreviewHandler provides AsyncImagePreviewHandler { request -> (request.data as? Drawable)?.asImage() },
        ) {
            MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme, content = content)
        }
    }

    private inner class StoreApp(
        val name: String,
        val color: Long,
        val usageMinutes: Long,
    ) {
        val packageName = "com.example.${name.lowercase()}"

        // Paparazzi can't render real launcher icons, so each app gets a letter tile.
        fun toInstalledApp() =
            InstalledApp(
                name = name,
                packageName = packageName,
                icon = letterTile(name.first(), color.toInt()),
                usageMillis = usageMinutes * 60_000L,
            )

        private fun letterTile(
            letter: Char,
            color: Int,
        ): Drawable {
            val size = 192
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color }
            canvas.drawRoundRect(RectF(0f, 0f, size.toFloat(), size.toFloat()), size * 0.28f, size * 0.28f, paint)
            paint.apply {
                this.color = Color.WHITE
                textSize = size * 0.5f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(letter.toString(), size / 2f, size / 2f - (paint.descent() + paint.ascent()) / 2, paint)
            return BitmapDrawable(paparazzi.resources, bitmap)
        }
    }
}

/** Writes each snapshot to `<dir>/<snapshot name>.png`. */
private class StoreScreenshotHandler(
    private val dir: File,
) : SnapshotHandler {
    override fun newFrameHandler(
        snapshot: Snapshot,
        frameCount: Int,
        fps: Int,
    ): SnapshotHandler.FrameHandler =
        object : SnapshotHandler.FrameHandler {
            override fun handle(image: BufferedImage) {
                dir.mkdirs()
                ImageIO.write(image, "png", File(dir, "${snapshot.name}.png"))
            }

            override fun close() = Unit
        }

    override fun close() = Unit
}
