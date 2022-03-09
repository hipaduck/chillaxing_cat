package com.peopleofandroido.chillaxingcat.domain.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.model.Period

interface DayOffRepository {
    suspend fun addDayOff(dateModel: DateModel): ResultModel<String>
    suspend fun getDayOff(id: Int): ResultModel<DateModel>
    suspend fun removeDayOff(dateModel: DateModel): ResultModel<String>
    suspend fun getDayOffWithPeriod(startPeriod: Period, endPeriod: Period): ResultModel<List<DateModel>>
}