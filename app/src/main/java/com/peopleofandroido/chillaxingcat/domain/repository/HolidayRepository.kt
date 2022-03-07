package com.peopleofandroido.chillaxingcat.domain.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.model.DayInfoList

interface HolidayRepository {
    //api 조회
    suspend fun getHolidaysInMonth(year: String, month: String): ResultModel<DayInfoList>
    //db
    suspend fun addHoliday(dateModel: DateModel)
    suspend fun getHoliday(id: Int): List<DateModel>
}