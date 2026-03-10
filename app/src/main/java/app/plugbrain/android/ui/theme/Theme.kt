package app.plugbrain.android.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Primary500,
    secondary = Primary400,
    surface = Neutral700,
    onSurface = Primary100,
    onPrimary = White,
    onSecondary = Primary100,
    background = Neutral900,
    error = Error600,
    outline = Neutral200,
    outlineVariant = Neutral500,
    surfaceVariant = Neutral500,
    onSurfaceVariant = Neutral300,
    tertiary = Success300,
    onTertiary = White,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Primary500,
    secondary = Primary400,
    surface = White,
    onSurface = Primary900,
    onPrimary = White,
    onSecondary = Primary900,
    background = White,
    error = Error500,
    outline = Neutral300,
    outlineVariant = Neutral200,
    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral400,
    tertiary = Success500,
    onTertiary = White,
  )

@Composable
fun MathlockAppTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content,
  )
}
