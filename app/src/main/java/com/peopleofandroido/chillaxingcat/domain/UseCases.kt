package com.peopleofandroido.chillaxingcat.domain

import com.peopleofandroido.chillaxingcat.domain.usecase.*

data class UseCases (
    val addDayOff: AddDayOff,
    val getDayOff: GetDayOff,
    val removeDayOff: RemoveDayOff,
    val getDayOffWithPeriod: GetDayOffWithPeriod,
    val addHoliday: AddHoliday,
    val getHolidayWithPeriod: GetHolidayWithPeriod,
    val getHoliday: GetHoliday,
    val getRestingTime: GetRestingTime,
    val addRestingTime: AddRestingTime,
    val editRestingTime: EditRestingTime,
)