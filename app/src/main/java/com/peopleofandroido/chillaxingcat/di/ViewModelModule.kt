package com.peopleofandroido.chillaxingcat.di

import com.peopleofandroido.chillaxingcat.presentation.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appViewModelModule = module {
    viewModel { MainMenuViewModel(get(), get()) }
    viewModel { SplashViewModel(get()) }
}