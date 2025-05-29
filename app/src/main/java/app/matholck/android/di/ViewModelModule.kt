package app.matholck.android.di

import app.matholck.android.ui.challenges.presentation.ArithChallengeViewModel
import app.matholck.android.ui.main.presentation.MainScreenViewModel
import app.matholck.android.ui.selectapps.presentation.AppsSelectionViewModel
import app.matholck.android.ui.settings.presentation.SettingsViewModel
import app.matholck.android.ui.timer.presentation.TimerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { MainScreenViewModel(get(), get(), get()) }
  viewModel { SettingsViewModel(get(), get(), get()) }
  viewModel { AppsSelectionViewModel(get(), get()) }
  viewModel { ArithChallengeViewModel(get(), get()) }
  viewModel { TimerViewModel(get()) }
}
