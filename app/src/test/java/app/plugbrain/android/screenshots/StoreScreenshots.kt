package app.plugbrain.android.screenshots

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
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
import app.plugbrain.android.ui.theme.PlugBrainTheme
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// Fastlane metadata folder to the app's resource locale, for every language the app is translated to.
private val storeLocales =
    listOf(
        "en-US" to "en",
        "ar" to "ar",
        "de-DE" to "de",
        "fa" to "fa",
        "fr-FR" to "fr",
        "hi" to "hi",
        "ja" to "ja",
        "nb-NO" to "nb",
        "nl-NL" to "nl",
        "ru-RU" to "ru",
        "zh-CN" to "zh-rCN",
    )

/**
 * The store screenshots, one set per fastlane locale folder, at full Pixel 5 resolution.
 * They only run with STORE_SCREENSHOTS=true: the "Store screenshots" workflow records them with
 * Paparazzi and moves each `<folder>_<n>` snapshot to `<folder>/images/phoneScreenshots/<n>.png`.
 */
class StoreScreenshots {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            supportsRtl = true,
            useDeviceResolution = true,
        )

    @Before
    fun onlyWhenAsked() {
        assumeTrue(System.getenv("STORE_SCREENSHOTS") == "true")
    }

    @Test
    fun welcome() =
        snapshotInEveryLocale(1) {
            WelcomeScreen(onGetStartedClick = {})
        }

    @Test
    fun chooseApps() = snapshotInEveryLocale(2) { appsStep() }

    @Test
    fun dashboardOn() = snapshotInEveryLocale(3) { dashboard() }

    @Test
    fun challenge() =
        snapshotInEveryLocale(4) {
            NumericalChallengeScreen(
                challenge = challenge,
                unlockMinutes = 15,
                onUnlockApp = {},
                onExitChallenge = {},
            )
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun challengesSheet() =
        snapshotInEveryLocale(5) {
            Box {
                dashboardBehindSheet()
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun minDifficultySheet() =
        snapshotInEveryLocale(6) {
            Box {
                dashboardBehindSheet()
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

    @Test
    fun dashboardDark() = snapshotInEveryLocale(7, darkTheme = true) { dashboard() }

    private fun snapshotInEveryLocale(
        number: Int,
        darkTheme: Boolean = false,
        content: @Composable () -> Unit,
    ) {
        storeLocales.forEach { (folder, locale) ->
            paparazzi.unsafeUpdateConfig(deviceConfig = DeviceConfig.PIXEL_5.copy(locale = locale))
            paparazzi.snapshot(name = "${folder}_$number") {
                themed(darkTheme = darkTheme, content = content)
            }
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
            StoreApp("TikTok", 0xFF059669, 72),
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
            onAppsClick = {},
            onBlockIntervalClick = {},
            onMinDifficultyClick = {},
            onBatteryOptimizationClick = {},
            onTurnBackOn = {},
            onAboutClick = {},
        )
    }

    // The sheet's own scrim fades in, so the snapshot draws it by hand.
    @Composable
    private fun dashboardBehindSheet() {
        dashboard()
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.32f)))
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
            PlugBrainTheme(dynamicColor = false, darkTheme = darkTheme, content = content)
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
                this.color = android.graphics.Color.WHITE
                textSize = size * 0.5f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(letter.toString(), size / 2f, size / 2f - (paint.descent() + paint.ascent()) / 2, paint)
            return BitmapDrawable(paparazzi.resources, bitmap)
        }
    }
}
