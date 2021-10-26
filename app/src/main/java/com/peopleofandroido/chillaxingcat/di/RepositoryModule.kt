package com.peopleofandroido.chillaxingcat.di

import com.peopleofandroido.chillaxingcat.data.remote.api.HolidayApi
import com.peopleofandroido.chillaxingcat.data.repository.FreeDayRepositoryImpl
import com.peopleofandroido.chillaxingcat.data.repository.UserInfoRepositoryImpl
import com.peopleofandroido.chillaxingcat.domain.repository.FreeDayRepository
import com.peopleofandroido.chillaxingcat.domain.repository.UserInfoRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val repositoryModule = module {
    single(createdAtStart = false) {
        get<Retrofit>()
            .create(HolidayApi::class.java)
    }

    single<UserInfoRepository> {
        UserInfoRepositoryImpl(get())
    }

    single<FreeDayRepository> {
        FreeDayRepositoryImpl(get(), get())
    }
}