package app.matholck.android.di

import app.matholck.android.ui.selectapps.presentation.AppsSelectionViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AppsSelectionViewModel(androidApplication(), get()) }
}
