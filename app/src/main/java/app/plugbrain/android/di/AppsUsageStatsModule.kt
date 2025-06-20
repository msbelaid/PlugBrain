package app.plugbrain.android.di

import app.plugbrain.android.appsusage.AppsUsageStats
import org.koin.dsl.module

val appsUsageModule = module {
  single { AppsUsageStats(get()) }
}
