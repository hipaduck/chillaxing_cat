package com.peopleofandroido.chillaxingcat.domain

import com.peopleofandroido.chillaxingcat.domain.usecase.*

data class UseCases (
    val addDayOff: AddDayOff,
    val getDayOff: GetDayOff,
    val removeDayOff: RemoveDayOff,
    val addHoliday: AddHoliday,
    val requestHoliday: RequestHoliday,
    val getHoliday: GetHoliday,
    val getRestingTime: GetRestingTime,
    val addRestingTime: AddRestingTime,
    val editRestingTime: EditRestingTime,
)