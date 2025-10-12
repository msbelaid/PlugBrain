package app.plugbrain.android.util

import android.content.Context
import app.plugbrain.android.R
import kotlin.time.Duration

object DurationFormatter {
    
    fun formatDuration(context: Context, duration: Duration): String {
        val totalMinutes = duration.inWholeMinutes
        
        return when {
            totalMinutes < 60 -> {
                // Less than 1 hour - show in minutes
                context.resources.getQuantityString(R.plurals.duration_minutes, totalMinutes.toInt(), totalMinutes)
            }
            totalMinutes < 1440 -> {
                // Less than 1 day - show in hours and minutes
                val hours = totalMinutes / 60
                val minutes = totalMinutes % 60
                if (minutes == 0L) {
                    context.resources.getQuantityString(R.plurals.duration_hours, hours.toInt(), hours)
                } else {
                    context.getString(R.string.duration_hours_minutes, 
                        context.resources.getQuantityString(R.plurals.duration_hours, hours.toInt(), hours),
                        context.resources.getQuantityString(R.plurals.duration_minutes, minutes.toInt(), minutes)
                    )
                }
            }
            else -> {
                // 1 day or more - show in days and hours
                val days = totalMinutes / 1440
                val hours = (totalMinutes % 1440) / 60
                if (hours == 0L) {
                    context.resources.getQuantityString(R.plurals.duration_days, days.toInt(), days)
                } else {
                    context.getString(R.string.duration_days_hours,
                        context.resources.getQuantityString(R.plurals.duration_days, days.toInt(), days),
                        context.resources.getQuantityString(R.plurals.duration_hours, hours.toInt(), hours)
                    )
                }
            }
        }
    }
}