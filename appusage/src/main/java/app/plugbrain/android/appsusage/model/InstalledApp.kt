package app.plugbrain.android.appsusage.model

import android.graphics.drawable.Drawable

data class InstalledApp(
  val name: String,
  val packageName: String,
  val icon: Drawable? = null,
  val usageMillis: Long = 0L,
)
