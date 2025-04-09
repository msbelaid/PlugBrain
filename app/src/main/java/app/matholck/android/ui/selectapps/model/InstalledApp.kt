package app.matholck.android.ui.selectapps.model

import android.graphics.drawable.Drawable

data class InstalledApp(
    val name: String,
    val packageName: String,
    val icon: Drawable,
    val selected: Boolean = false
)
