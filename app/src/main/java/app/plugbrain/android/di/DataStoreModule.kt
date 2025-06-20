package app.plugbrain.android.di

import app.plugbrain.android.datastore.DataStoreManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
  single { DataStoreManager(androidContext()) }
}
