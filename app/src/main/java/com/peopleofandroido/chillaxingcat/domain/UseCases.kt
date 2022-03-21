package com.peopleofandroido.chillaxingcat.domain

import com.peopleofandroido.chillaxingcat.domain.usecase.*

data class UseCases (
    val addDayOff: AddDayOff,
    val getDayOff: GetDayOff,
    val removeDayOff: RemoveDayOff,
//    val getDayOffWithPeriod: GetDayOffWithPeriod,
    val addHoliday: AddHoliday,
    val getHolidayWithPeriod: GetHolidayWithPeriod,
    val getHoliday: GetHoliday,
    val getRestingTime: GetRestingTime,
    val addRestingTime: AddRestingTime,
    val editRestingTime: EditRestingTime,
    val findOutRestingDaysInMonth: FindOutRestingDaysInMonth, // 월을 주면(eg. 202203) 리스트로 조금이라도 쉬었던 날에 대한 목록을 돌려주는 UseCase 필요
)