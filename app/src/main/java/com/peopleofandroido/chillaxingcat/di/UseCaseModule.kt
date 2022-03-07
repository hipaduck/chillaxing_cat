package com.peopleofandroido.chillaxingcat.di

import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single {
        UseCases(get(), get(), get(), get(), get(), get(), get(), get(), get())
    }

    single { AddDayOff(get()) }
    single { GetDayOff(get()) }
    single { RequestHoliday(get(), get()) }
    single { GetHoliday(get()) }
    single { AddHoliday(get()) }
    single { RemoveDayOff(get()) }
    single { AddRestingTime(get()) }
    single { EditRestingTime(get()) }
    single { GetRestingTime(get()) }
}