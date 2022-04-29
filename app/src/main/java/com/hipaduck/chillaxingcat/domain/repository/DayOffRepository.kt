package com.hipaduck.chillaxingcat.domain.repository

import com.hipaduck.base.domain.model.ResultModel
import com.hipaduck.chillaxingcat.domain.model.DateModel

interface DayOffRepository {
    suspend fun addDayOff(dateModel: DateModel): ResultModel<String>
    suspend fun getDayOff(id: Int): ResultModel<DateModel>
    suspend fun removeDayOff(dateModel: DateModel): ResultModel<String>
//    suspend fun getDayOffWithPeriod(startPeriod: Period, endPeriod: Period): ResultModel<List<DateModel>>
}