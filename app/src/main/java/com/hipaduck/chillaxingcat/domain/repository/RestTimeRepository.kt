package com.hipaduck.chillaxingcat.domain.repository

import com.hipaduck.base.domain.model.ResultModel
import com.hipaduck.chillaxingcat.domain.model.RestTimeModel

interface RestTimeRepository {
    suspend fun addRestingTime(restTime: RestTimeModel): ResultModel<String>
    suspend fun editTotalTime(id: Int, totalTime: Long): ResultModel<Boolean>
    suspend fun getRestingTime(id: Int): ResultModel<RestTimeModel>
    suspend fun getRestingTimeInMonth(month: String): ResultModel<List<RestTimeModel>>
}