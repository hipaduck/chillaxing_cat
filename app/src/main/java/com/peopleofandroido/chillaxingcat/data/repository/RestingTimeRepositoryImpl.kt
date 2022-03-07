package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.chillaxingcat.data.dao.RestingTimeDao
import com.peopleofandroido.chillaxingcat.data.entity.RestingTime
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository

class RestingTimeRepositoryImpl(private val restingTimeDao: RestingTimeDao) : RestingTimeRepository {
    override suspend fun addRestingTime(restingTime: RestingTimeModel)
        = restingTimeDao.insert(RestingTimeDao.fromDomainModel(restingTime))

    override suspend fun editRestingTime(id: Int, history: String) {
        restingTimeDao.editRestingTime(id, history)
    }

    override suspend fun getRestingTime(id: Int): List<RestingTimeModel> {
        return restingTimeDao.getRestingTime(id)
    }


}
