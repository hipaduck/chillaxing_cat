package com.peopleofandroido.chillaxingcat.di

import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single {
        UseCases(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }

    single { AddDayOff(get(), get()) }
    single { GetDayOff(get(), get()) }
    single { GetHolidayWithPeriod(get(), get()) }
    single { GetHoliday(get(), get()) }
    single { AddHoliday(get(), get()) }
    single { RemoveDayOff(get(), get()) }
    single { AddRestingTime(get(), get()) }
    single { EditRestingTime(get(), get()) }
    single { GetRestingTime(get(), get()) }
    single { FindOutRestingDaysInMonth(get(), get()) }
    single { GetNotificationStatus(get(), get()) }
    single { PutNotificationStatus(get(), get()) }
    single { GetReminderText(get(), get()) }
    single { PutReminderText(get(), get()) }
    single { GetReminderTime(get(), get()) }
    single { PutReminderTime(get(), get()) }
    single { GetGoalRestingTime(get(), get()) }
    single { PutGoalRestingTime(get(), get()) }
    single { IsRequiredValuesEntered(get(), get()) }
}