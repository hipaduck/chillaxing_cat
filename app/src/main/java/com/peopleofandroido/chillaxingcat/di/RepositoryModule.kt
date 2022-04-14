package com.peopleofandroido.chillaxingcat.di

import com.peopleofandroido.chillaxingcat.data.remote.api.HolidayApi
import com.peopleofandroido.chillaxingcat.data.repository.*
import com.peopleofandroido.chillaxingcat.domain.repository.*
import org.koin.dsl.module
import retrofit2.Retrofit

val repositoryModule = module {
    single(createdAtStart = false) {
        get<Retrofit>()
            .create(HolidayApi::class.java)
    }

    single<RestingTimeRepository> {
        RestingTimeRepositoryImpl(get())
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