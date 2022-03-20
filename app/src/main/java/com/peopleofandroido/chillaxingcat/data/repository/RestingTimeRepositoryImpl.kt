package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.data.dao.RestingTimeDao
import com.peopleofandroido.chillaxingcat.data.entity.DayOff
import com.peopleofandroido.chillaxingcat.data.entity.RestingTime
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RestingTimeRepositoryImpl(private val restingTimeDao: RestingTimeDao) : RestingTimeRepository {
    override suspend fun addRestingTime(restingTime: RestingTimeModel): ResultModel<String> {
        var failMessage = ""

        val result: Boolean = try {
            restingTimeDao.insert(RestingTime.fromDomainModel(restingTime))
            true
        } catch (e: Exception) {
            failMessage = e.message!!
            false
        }

        return if(result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "while adding ${restingTime.id}")
        }
    }


    override suspend fun editRestingTime(id: Int, history: String): ResultModel<String> {
        var failMessage = ""

        val result: Boolean = try {
            restingTimeDao.editRestingTime(id, history)
            true
        } catch (e: Exception) {
            failMessage = e.message!!
            false
        }

        return if(result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "while editing $id")
        }
    }

    override suspend fun getRestingTime(id: Int): ResultModel<RestingTimeModel> {
        var failMessage = ""

        val restingTime: RestingTime
        var restingTimeModel: RestingTimeModel? = null

        val result: Boolean = try {
            restingTime  = restingTimeDao.getRestingTime(id)
            restingTimeModel = RestingTimeModel(restingTime.id, restingTime.history, restingTime.totalTime)
            true
        } catch (e: Exception) {
            failMessage = e.message!!
            false
        }

        return if(result) {
            ResultModel(0, "success", restingTimeModel)
        } else {
            ResultModel(1, failMessage, restingTimeModel)
        }
    }
}
