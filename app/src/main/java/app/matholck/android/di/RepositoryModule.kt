package app.matholck.android.di

import app.matholck.android.repository.InstalledAppsRepository
import app.matholck.android.repository.QuestionsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
  single { InstalledAppsRepository(androidContext()) }
  single { QuestionsRepository(get()) }
}
