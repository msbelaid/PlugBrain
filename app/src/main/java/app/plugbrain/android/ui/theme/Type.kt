package app.plugbrain.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.plugbrain.android.R

val SoraFontFamily = FontFamily(
  Font(R.font.sora_regular, FontWeight.Normal),
  Font(R.font.sora_medium, FontWeight.Medium),
  Font(R.font.sora_semi_bold, FontWeight.SemiBold),
  Font(R.font.sora_bold, FontWeight.Bold),
)

val Typography =
  Typography(

    displayLarge = TextStyle(
      fontFamily = SoraFontFamily,
      fontWeight = FontWeight.SemiBold,
      fontSize = 32.sp,
      lineHeight = 40.sp,
    ),

    headlineLarge = TextStyle(
      fontFamily = SoraFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 24.sp,
      lineHeight = 28.sp,
    ),

    titleLarge = TextStyle(
      fontFamily = SoraFontFamily,
      fontWeight = FontWeight.SemiBold,
      fontSize = 14.sp,
      lineHeight = 18.sp,
    ),

    bodyLarge = TextStyle(
      fontFamily = SoraFontFamily,
      fontWeight = FontWeight.Normal,
      fontSize = 16.sp,
      lineHeight = 22.sp,
    ),

    labelLarge = TextStyle(
      fontFamily = SoraFontFamily,
      fontWeight = FontWeight.SemiBold,
      fontSize = 14.sp,
      lineHeight = 18.sp,
    ),

    labelSmall = TextStyle(
      fontFamily = SoraFontFamily,
      fontWeight = FontWeight.SemiBold,
      fontSize = 14.sp,
      lineHeight = 18.sp,
    ),
  )
