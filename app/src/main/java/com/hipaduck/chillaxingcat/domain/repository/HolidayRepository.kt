package com.hipaduck.chillaxingcat.domain.repository

import com.hipaduck.base.domain.model.ResultModel
import com.hipaduck.chillaxingcat.domain.model.DateModel

interface HolidayRepository {
    //api 조회
    suspend fun getHolidayWithPeriod(startPeriod: String, endPeriod: String): ResultModel<List<DateModel>>
}