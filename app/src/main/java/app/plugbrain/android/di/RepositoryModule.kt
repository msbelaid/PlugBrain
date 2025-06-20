package app.plugbrain.android.di

import app.plugbrain.android.repository.InstalledAppsRepository
import app.plugbrain.android.repository.MathChallengeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
  single { InstalledAppsRepository(androidContext()) }
  single { MathChallengeRepository(get()) }
}
