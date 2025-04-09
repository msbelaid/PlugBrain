package app.matholck.android.di

import app.matholck.android.repository.InstalledAppsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
  single { InstalledAppsRepository(androidContext()) }
}
