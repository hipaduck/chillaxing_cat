package com.peopleofandroido.chillaxingcat.domain.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel

interface RestingTimeRepository {
    suspend fun addRestingTime(restingTime: RestingTimeModel): ResultModel<String>
    suspend fun editRestingTime(id: Int, history: String): ResultModel<String>
    suspend fun editTotalTime(id: Int, totalTime: Long): ResultModel<Boolean>
    suspend fun getRestingTime(id: Int): ResultModel<RestingTimeModel>
    suspend fun getRestingTimeInMonth(month: String): ResultModel<List<RestingTimeModel>>
}