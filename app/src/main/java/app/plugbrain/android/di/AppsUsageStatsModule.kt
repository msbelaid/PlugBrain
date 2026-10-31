package app.plugbrain.android.di

import app.plugbrain.android.appsusage.AppsUsageStats
import app.plugbrain.android.appsusage.SystemAppsUsageStats
import app.plugbrain.android.blocking.UsageLimit
import app.plugbrain.android.blocking.UsageSource
import app.plugbrain.android.util.Clock
import org.koin.dsl.module

val appsUsageModule = module {
  single<Clock> { Clock.System }
  single<AppsUsageStats> { SystemAppsUsageStats(get()) }
  factory<UsageSource> {
    val appsUsageStats: AppsUsageStats = get()
    UsageSource { packages, from, to -> appsUsageStats.getTotalAppsUsageDuration(from, to, packages) }
  }
  factory { UsageLimit(get()) }
}
