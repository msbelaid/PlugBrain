package app.plugbrain.android.di

import app.plugbrain.android.repository.InstalledAppsRepository
import app.plugbrain.android.repository.MathChallengeRepository
import app.plugbrain.android.repository.PermissionsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
  single { InstalledAppsRepository(androidContext()) }
  single { MathChallengeRepository(get(), get()) }
  single { PermissionsRepository(androidContext()) }
}
