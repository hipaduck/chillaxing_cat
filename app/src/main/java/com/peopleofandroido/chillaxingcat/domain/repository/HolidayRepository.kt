package com.peopleofandroido.chillaxingcat.domain.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.DateModel

interface HolidayRepository {
    //api 조회
    suspend fun getHolidayWithPeriod(startPeriod: String, endPeriod: String): ResultModel<List<DateModel>>
    //db
    suspend fun addHoliday(dateModel: DateModel): ResultModel<String>
    suspend fun getHoliday(id: Int): ResultModel<DateModel>

}