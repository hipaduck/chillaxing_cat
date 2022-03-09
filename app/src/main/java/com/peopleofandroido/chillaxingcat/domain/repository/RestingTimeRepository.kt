package com.peopleofandroido.chillaxingcat.domain.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel

interface RestingTimeRepository {
    suspend fun addRestingTime(restingTime: RestingTimeModel): ResultModel<String>
    suspend fun editRestingTime(id: Int, history: String): ResultModel<String>
    suspend fun getRestingTime(id: Int): ResultModel<RestingTimeModel>
}