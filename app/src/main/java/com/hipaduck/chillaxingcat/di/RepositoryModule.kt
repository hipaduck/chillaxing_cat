package com.hipaduck.chillaxingcat.di

import com.hipaduck.chillaxingcat.data.remote.api.HolidayApi
import com.hipaduck.chillaxingcat.data.repository.*
import com.hipaduck.chillaxingcat.domain.repository.*
import org.koin.dsl.module
import retrofit2.Retrofit

val repositoryModule = module {
    single(createdAtStart = false) {
        get<Retrofit>()
            .create(HolidayApi::class.java)
    }

    single<RestTimeRepository> {
        RestTimeRepositoryImpl(get())
    }

    single<HolidayRepository> {
        HolidayRepositoryImpl(get(), get(), get())
    }

    single<DayOffRepository> {
        DayOffRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<TodayDataRepository> {
        TodayDataRepositoryImpl(get())
    }
}