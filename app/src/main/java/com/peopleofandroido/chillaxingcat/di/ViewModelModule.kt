package com.peopleofandroido.chillaxingcat.di

import com.peopleofandroido.chillaxingcat.presentation.viewmodel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appViewModelModule = module {
    viewModel { MainMenuViewModel(androidApplication(),get(), get()) }
    viewModel { SettingViewModel(androidApplication(), get()) }
    viewModel { CalendarViewModel(get(), get()) }
    viewModel { UserSettingViewModel(androidApplication(), get(), get()) }
    viewModel { SplashViewModel(get()) }
}