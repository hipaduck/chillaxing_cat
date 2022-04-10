package com.peopleofandroido.chillaxingcat.di

import com.peopleofandroido.chillaxingcat.data.local.SettingsDataStore
import com.peopleofandroido.chillaxingcat.data.local.TodayDataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appDataStoreModule = module {
    single {
        SettingsDataStore(androidApplication())
    }

    single {
        TodayDataStore(androidApplication())
    }
}