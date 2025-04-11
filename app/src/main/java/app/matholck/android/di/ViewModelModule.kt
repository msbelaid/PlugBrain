package app.matholck.android.di

import app.matholck.android.ui.selectapps.presentation.AppsSelectionViewModel
import app.matholck.android.ui.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { AppsSelectionViewModel(get(), get()) }
  viewModel { SettingsViewModel(get(), get(), get()) }
}
