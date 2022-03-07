package com.peopleofandroido.chillaxingcat.domain.repository

import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel

interface RestingTimeRepository {
    suspend fun addRestingTime(restingTime: RestingTimeModel)
    suspend fun editRestingTime(id: Int, history: String)
    suspend fun getRestingTime(id: Int): List<RestingTimeModel>
}