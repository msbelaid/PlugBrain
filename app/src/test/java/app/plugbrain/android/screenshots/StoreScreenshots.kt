package app.plugbrain.android.screenshots

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.R
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.challenges.addition.AdditionChallenge
import app.plugbrain.android.repository.model.PermissionsState
import app.plugbrain.android.ui.blockinterval.BlockIntervalSheet
import app.plugbrain.android.ui.challenges.compose.NumericalChallengeScreen
import app.plugbrain.android.ui.main.compose.MainScreen
import app.plugbrain.android.ui.main.presentation.DashboardStatus
import app.plugbrain.android.ui.main.presentation.MainScreenState
import app.plugbrain.android.ui.mindifficulty.MinDifficultySheet
import app.plugbrain.android.ui.onboarding.AppsStep
import app.plugbrain.android.ui.onboarding.PermissionsStep
import app.plugbrain.android.ui.onboarding.PreferencesStep
import app.plugbrain.android.ui.onboarding.WelcomeScreen
import app.plugbrain.android.ui.onboarding.presentation.DifficultyPreset
import app.plugbrain.android.ui.selectapps.presentation.InstalledAppsState
import app.plugbrain.android.ui.theme.Neutral900
import app.plugbrain.android.ui.theme.PlugBrainTheme
import app.plugbrain.android.ui.theme.Primary100
import app.plugbrain.android.ui.theme.Primary300
import app.plugbrain.android.ui.theme.Primary600
import app.plugbrain.android.ui.theme.Primary700
import app.plugbrain.android.ui.theme.SoraFontFamily
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
 *
 * The `play...` tests render the Google Play set: 1080x1920, a caption over each screen in a phone frame.
 * The workflow moves each `<folder>_play<n>` snapshot to `fastlane/play-images/<folder>/phoneScreenshots/<n>.png`.
 */
class StoreScreenshots {
    @get:Rule
    val paparazzi =
        Paparazzi(
            environment = paparazziEnvironment(),
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

    @Test
    fun playChallenge() =
        playInEveryLocale(1, PlayBackground.Dark) {
            NumericalChallengeScreen(
                challenge = challenge,
                unlockMinutes = 15,
                onUnlockApp = {},
                onExitChallenge = {},
            )
        }

    @Test
    fun playDashboardOn() = playInEveryLocale(2, PlayBackground.Light) { dashboard() }

    @Test
    fun playDashboardLocked() =
        playInEveryLocale(3, PlayBackground.Teal) {
            dashboard(DashboardStatus.AppsLocked(awayMinutes = 6, challengeLevel = 5))
        }

    @Test
    fun playChooseApps() = playInEveryLocale(4, PlayBackground.Light) { appsStep() }

    @Test
    fun playPreferences() =
        playInEveryLocale(5, PlayBackground.Dark) {
            PreferencesStep(
                blockInterval = 5,
                difficulty = DifficultyPreset.Beginner,
                onBlockIntervalSelect = {},
                onDifficultySelect = {},
                onFinish = {},
                onSkip = {},
            )
        }

    @Test
    fun playPermissions() =
        playInEveryLocale(6, PlayBackground.Light) {
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

    @Test
    fun playDashboardDark() = playInEveryLocale(7, PlayBackground.Dark, darkTheme = true) { dashboard() }

    private enum class PlayBackground(
        val brush: Brush,
        val eyebrow: Color,
        val headline: Color,
    ) {
        Dark(Brush.verticalGradient(listOf(Color(0xFF07343B), Color(0xFF021A1F))), Primary300, Color.White),
        Teal(Brush.verticalGradient(listOf(Primary600, Primary700)), Primary100, Color.White),
        Light(Brush.verticalGradient(listOf(Color(0xFFF7F3F2), Color(0xFFF7F3F2))), Primary600, Neutral900),
    }

    private fun playInEveryLocale(
        number: Int,
        background: PlayBackground,
        darkTheme: Boolean = false,
        screen: @Composable () -> Unit,
    ) {
        storeLocales.forEach { (folder, locale) ->
            paparazzi.unsafeUpdateConfig(
                deviceConfig = DeviceConfig.PIXEL_5.copy(locale = locale, screenHeight = PLAY_HEIGHT_PX),
            )
            val caption = playCaptions.getValue(folder)[number - 1]
            paparazzi.snapshot(name = "${folder}_play$number") {
                themed {
                    playFrame(caption = caption, background = background) {
                        themed(darkTheme = darkTheme, content = screen)
                    }
                }
            }
        }
    }

    private val captionStyle = TextStyle(fontFamily = SoraFontFamily, fontWeight = FontWeight.SemiBold)

    // A caption over the app screen, shown in a phone frame that runs off the bottom edge.
    @Composable
    private fun playFrame(
        caption: PlayCaption,
        background: PlayBackground,
        screen: @Composable () -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(background.brush).padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = caption.eyebrow.uppercase(),
                modifier = Modifier.padding(horizontal = 24.dp),
                // Letter spacing only suits scripts with capitals; it breaks up joined scripts like Arabic.
                style =
                    captionStyle.copy(
                        fontSize = 11.sp,
                        letterSpacing = if (caption.eyebrow.uppercase() != caption.eyebrow) 0.12.em else 0.em,
                    ),
                color = background.eyebrow,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = caption.headline.ifEmpty { stringResource(R.string.welcome_title) },
                modifier = Modifier.padding(horizontal = 28.dp),
                style = captionStyle.copy(fontSize = 26.sp, lineHeight = 32.sp),
                color = background.headline,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(28.dp))
            val phoneWidth = LocalConfiguration.current.screenWidthDp.dp
            val frameWidth = phoneWidth * PLAY_FRAME_SHARE
            val bezel = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
            Box(
                modifier =
                    Modifier
                        .width(frameWidth)
                        .weight(1f)
                        .clip(bezel)
                        .background(Color(0xFF0A1414))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), bezel)
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
                ) {
                    // The screen keeps its real phone size and is scaled down to fit the frame.
                    Box(
                        modifier =
                            Modifier
                                .wrapContentSize(Alignment.TopCenter, unbounded = true)
                                .requiredSize(phoneWidth, phoneWidth * PHONE_ASPECT)
                                .graphicsLayer {
                                    val scale = (frameWidth - 16.dp) / phoneWidth
                                    scaleX = scale
                                    scaleY = scale
                                    transformOrigin = TransformOrigin(0.5f, 0f)
                                },
                    ) {
                        screen()
                    }
                }
            }
        }
    }

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
    private fun dashboard(
        status: DashboardStatus =
            DashboardStatus.Active(
                usedMinutes = 4,
                blockInterval = 15,
                nextChallengeLevel = 4,
                maxChallengeLevel = 15,
            ),
    ) {
        MainScreen(
            state =
                MainScreenState(
                    status = status,
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
        // Coil needs an image for every request, so anything that isn't an icon draws nothing.
        val previewHandler =
            AsyncImagePreviewHandler { request ->
                val drawable = request.data as? Drawable ?: ColorDrawable()
                drawable.asImage()
            }
        CompositionLocalProvider(
            LocalInspectionMode provides true,
            LocalAsyncImagePreviewHandler provides previewHandler,
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

// Google Play phone screenshots are 9:16, so the Pixel 5 canvas is cut to 1080x1920 px.
private const val PLAY_HEIGHT_PX = 1920

// The phone frame takes this share of the width; the screen inside keeps the Pixel 5's 1080:2340 shape.
private const val PLAY_FRAME_SHARE = 0.76f
private const val PHONE_ASPECT = 2340f / 1080f
