package app.plugbrain.android.di

import app.plugbrain.android.ui.challenges.presentation.ArithChallengeViewModel
import app.plugbrain.android.ui.main.presentation.MainScreenViewModel
import app.plugbrain.android.ui.selectapps.presentation.AppsSelectionViewModel
import app.plugbrain.android.ui.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { MainScreenViewModel(get(), get(), get(), get(), get()) }
  viewModel { SettingsViewModel(get(), get(), get(), get()) }
  viewModel { AppsSelectionViewModel(get(), get()) }
  viewModel { ArithChallengeViewModel(get(), get()) }
}
