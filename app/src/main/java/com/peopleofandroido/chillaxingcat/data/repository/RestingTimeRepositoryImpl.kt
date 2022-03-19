package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.data.dao.RestingTimeDao
import com.peopleofandroido.chillaxingcat.data.entity.RestingTime
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RestingTimeRepositoryImpl(private val restingTimeDao: RestingTimeDao) : RestingTimeRepository {
    override suspend fun addRestingTime(restingTime: RestingTimeModel): ResultModel<String> {
        restingTimeDao.insert(RestingTime.fromDomainModel(restingTime))
        return ResultModel(0, "success", "success")
    }


    override suspend fun editRestingTime(id: Int, history: String): ResultModel<String> {
        restingTimeDao.editRestingTime(id, history)
        return ResultModel(0, "success", "success")
    }

    override suspend fun getRestingTime(id: Int): ResultModel<RestingTimeModel> {
        val restingTime  = restingTimeDao.getRestingTime(id)
        val restingTimeModel = RestingTimeModel(restingTime.id, restingTime.history, restingTime.totalTime)

        return ResultModel(0, "success", restingTimeModel)
    }


}
