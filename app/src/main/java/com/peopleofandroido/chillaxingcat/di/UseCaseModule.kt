package com.peopleofandroido.chillaxingcat.di

import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single {
        UseCases(get(), get(), get(), get(), get())
    }

    single { AddFreeDay() }
    single { GetFreeDay() }
    single { GetHoliday(get(), get()) }
    single { GetUserInfo() }
    single { RemoveFreeDay() }
}