package app.plugbrain.android.util

import android.content.Context
import app.plugbrain.android.R
import kotlin.time.Duration

private const val MINUTES_IN_HOUR = 60
private const val MINUTES_IN_DAY = 1440

fun Context.formatDuration(duration: Duration): String {
    val totalMinutes = duration.inWholeMinutes
    
    return when {
        totalMinutes < MINUTES_IN_HOUR -> {
            // Less than 1 hour - show in minutes
            resources.getQuantityString(R.plurals.duration_minutes, totalMinutes.toInt(), totalMinutes)
        }
        totalMinutes < MINUTES_IN_DAY -> {
            // Less than 1 day - show in hours and minutes
            val hours = totalMinutes / MINUTES_IN_HOUR
            val minutes = totalMinutes % MINUTES_IN_HOUR
            if (minutes == 0L) {
                resources.getQuantityString(R.plurals.duration_hours, hours.toInt(), hours)
            } else {
                getString(R.string.duration_hours_minutes, 
                    resources.getQuantityString(R.plurals.duration_hours, hours.toInt(), hours),
                    resources.getQuantityString(R.plurals.duration_minutes, minutes.toInt(), minutes)
                )
            }
        }
        else -> {
            // 1 day or more - show in days and hours
            val days = totalMinutes / MINUTES_IN_DAY
            val hours = (totalMinutes % MINUTES_IN_DAY) / MINUTES_IN_HOUR
            if (hours == 0L) {
                resources.getQuantityString(R.plurals.duration_days, days.toInt(), days)
            } else {
                getString(R.string.duration_days_hours,
                    resources.getQuantityString(R.plurals.duration_days, days.toInt(), days),
                    resources.getQuantityString(R.plurals.duration_hours, hours.toInt(), hours)
                )
            }
        }
    }
}