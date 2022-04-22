package com.hipaduck.chillaxingcat.di

import com.hipaduck.chillaxingcat.domain.UseCases
import com.hipaduck.chillaxingcat.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single {
        UseCases(get(), get(), get(), get(), get(), get(), get(), get(),
            get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),
            get(), get(), get(), get(), get()
        )
    }

    single { AddDayOff(get(), get()) }
    single { GetDayOff(get(), get()) }
    single { GetHolidayWithPeriod(get(), get()) }
    single { RemoveDayOff(get(), get()) }
    single { AddRestTime(get(), get()) }
    single { GetRestTime(get(), get()) }
    single { FindOutRestDaysInMonth(get(), get()) }
    single { GetNotificationStatus(get(), get()) }
    single { PutNotificationStatus(get(), get()) }
    single { GetReminderText(get(), get()) }
    single { PutReminderText(get(), get()) }
    single { GetReminderTime(get(), get()) }
    single { PutReminderTime(get(), get()) }
    single { GetGoalRestTime(get(), get()) }
    single { PutGoalRestingTime(get(), get()) }
    single { IsRequiredValuesEntered(get(), get()) }
    single { WriteRestTotalTime(get(), get()) }
    single { GetTodayDate(get(), get()) }
    single { PutTodayDate(get(), get()) }
    single { GetTodayHistory(get(), get()) }
    single { PutTodayHistory(get(), get()) }
    single { GetTodayStatus(get(), get()) }
    single { PutTodayStatus(get(), get()) }
}