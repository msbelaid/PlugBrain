package app.matholck.android

import android.app.Application
import app.matholck.android.di.appsUsageModule
import app.matholck.android.di.dataStoreModule
import app.matholck.android.di.repositoryModule
import app.matholck.android.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    // TODO use Timber only on debug mode.
    Timber.plant(Timber.DebugTree())
    startKoin {
      androidLogger()
      androidContext(this@MainApplication)
      modules(dataStoreModule, viewModelModule, repositoryModule, appsUsageModule)
    }
  }
}
