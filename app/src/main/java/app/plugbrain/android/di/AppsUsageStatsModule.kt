package app.plugbrain.android.di

import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.appsusage.SystemAppsUsageStats
import app.plugbrain.android.service.UsageLimit
import app.plugbrain.android.util.Clock
import org.koin.dsl.module

val appsUsageModule = module {
  single<Clock> { Clock.System }
  single<AppsUsageStats> { SystemAppsUsageStats(get()) }
  factory { UsageLimit(get(), get()) }
}
