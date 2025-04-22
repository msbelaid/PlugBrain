package app.matholck.android.di

import app.matholck.android.appsusage.AppsUsageStats
import org.koin.dsl.module

val appsUsageModule = module {
  single { AppsUsageStats(get()) }
}
