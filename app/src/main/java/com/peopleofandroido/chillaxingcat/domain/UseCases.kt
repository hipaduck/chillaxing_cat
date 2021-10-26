package com.peopleofandroido.chillaxingcat.domain

import com.peopleofandroido.chillaxingcat.domain.usecase.*

data class UseCases (
    val addFreeDay: AddFreeDay,
    val getFreeDay: GetFreeDay,
    val getHoliday: GetHoliday,
    val getUserInfo: GetUserInfo,
    val removeFreeDay: RemoveFreeDay
)