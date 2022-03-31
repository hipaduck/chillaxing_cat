package com.peopleofandroido.chillaxingcat.di

import com.peopleofandroido.chillaxingcat.data.remote.api.HolidayApi
import com.peopleofandroido.chillaxingcat.data.repository.DayOffRepositoryImpl
import com.peopleofandroido.chillaxingcat.data.repository.HolidayRepositoryImpl
import com.peopleofandroido.chillaxingcat.data.repository.RestingTimeRepositoryImpl
import com.peopleofandroido.chillaxingcat.data.repository.SettingsRepositoryImpl
import com.peopleofandroido.chillaxingcat.domain.repository.DayOffRepository
import com.peopleofandroido.chillaxingcat.domain.repository.HolidayRepository
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository
import com.peopleofandroido.chillaxingcat.domain.repository.SettingsRepository
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
}