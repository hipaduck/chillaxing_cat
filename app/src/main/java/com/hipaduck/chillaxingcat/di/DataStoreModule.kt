package com.hipaduck.chillaxingcat.di

import com.hipaduck.chillaxingcat.data.local.SettingsDataStore
import com.hipaduck.chillaxingcat.data.local.TodayDataStore
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