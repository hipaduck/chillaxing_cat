package com.peopleofandroido.chillaxingcat.domain.repository

import com.peopleofandroido.chillaxingcat.domain.model.DateModel

interface DayOffRepository {
    suspend fun addDayOff(dateModel: DateModel)
    suspend fun getDayOff(id: Int): List<DateModel>
    suspend fun removeDayOff(dateModel: DateModel)
}