package app.plugbrain.android.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun isPortrait() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
