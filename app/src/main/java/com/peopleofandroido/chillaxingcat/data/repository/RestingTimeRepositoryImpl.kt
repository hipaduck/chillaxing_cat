package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.data.dao.RestingTimeDao
import com.peopleofandroido.chillaxingcat.data.entity.RestingTime
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RestingTimeRepositoryImpl(private val restingTimeDao: RestingTimeDao) : RestingTimeRepository {
    override suspend fun addRestingTime(restingTime: RestingTimeModel): ResultModel<String> {
        var failMessage = ""
        val result: Boolean = suspendCoroutine { cont->
            try {
                restingTimeDao.insert(RestingTime.fromDomainModel(restingTime))
                cont.resume(true)
            } catch (e: Exception) {
                failMessage = e.message!!
                cont.resume(false)
            }
        }
        return if (result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "fail adding RestingTime $restingTime")
        }
    }


    override suspend fun editRestingTime(id: Int, history: String): ResultModel<String> {
        var failMessage = ""
        val result: Boolean = suspendCoroutine { cont->
            try {
                restingTimeDao.editRestingTime(id, history)
                cont.resume(true)
            } catch (e: Exception) {
                failMessage = e.message!!
                cont.resume(false)
            }
        }
        return if (result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "fail editing RestingTime $id")
        }
    }

    override suspend fun getRestingTime(id: Int): ResultModel<RestingTimeModel> {
        var restingTimeModel : RestingTimeModel? = null

        var failMessage = ""
        val result: Boolean = suspendCoroutine { cont->
            try {
                val restingTime  = restingTimeDao.getRestingTime(id)
                restingTimeModel = RestingTimeModel(restingTime.id, restingTime.history, restingTime.totalTime)
                cont.resume(true)
            } catch (e: Exception) {
                failMessage = e.message!!
                cont.resume(false)
            }
        }

        return if (result) {
            ResultModel(0, "success", restingTimeModel)
        } else {
            ResultModel(1, failMessage, restingTimeModel)
        }
    }


}
