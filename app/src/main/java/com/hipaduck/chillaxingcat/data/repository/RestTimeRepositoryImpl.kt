package com.hipaduck.chillaxingcat.data.repository

import com.hipaduck.base.domain.model.ResultModel
import com.hipaduck.chillaxingcat.data.dao.RestTimeDao
import com.hipaduck.chillaxingcat.data.entity.RestTime
import com.hipaduck.chillaxingcat.domain.model.RestTimeModel
import com.hipaduck.chillaxingcat.domain.repository.RestTimeRepository

class RestTimeRepositoryImpl(private val restingTimeDao: RestTimeDao) : RestTimeRepository {
    companion object {
        const val FAIL_RETURN = -1
    }
    override suspend fun addRestingTime(restTime: RestTimeModel): ResultModel<String> {
        var failMessage = ""

        val result: Boolean = try {
            restingTimeDao.insert(RestTime.fromDomainModel(restTime))
            true
        } catch (e: Exception) {
            failMessage = e.message!!
            false
        }

        return if(result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "while adding ${restTime.id}")
        }
    }

    override suspend fun editTotalTime(id: Int, totalTime: Long): ResultModel<Boolean> {
        var failMessage = ""

        val result: Int = try {
            restingTimeDao.updateTotalTime(id, totalTime)
        } catch (e: Exception) {
            failMessage = e.message!!
            FAIL_RETURN
        }

        return if(result == FAIL_RETURN) {
            ResultModel(1, failMessage, false)
        } else {
            ResultModel(0, "success", true)
        }
    }

    override suspend fun getRestingTime(id: Int): ResultModel<RestTimeModel> {
        var failMessage = ""

        val restingTime: RestTime
        var restTimeModel: RestTimeModel? = null

        val result: Boolean = try {
            restingTime  = restingTimeDao.getRestingTime(id)
            restTimeModel = RestTimeModel(restingTime.id, restingTime.history, restingTime.totalTime)
            true
        } catch (e: Exception) {
            failMessage = e.message!!
            false
        }

        return if(result) {
            ResultModel(0, "success", restTimeModel)
        } else {
            ResultModel(1, failMessage, restTimeModel)
        }
    }

    override suspend fun getRestingTimeInMonth(month: String): ResultModel<List<RestTimeModel>> {
        var failMessage = ""

        val restingTimeList: List<RestTime>
        var restTimeModelList: List<RestTimeModel>? = null

        val result: Boolean = try {
            restingTimeList  = restingTimeDao.getRestingDaysInMonth(month)
            restTimeModelList = restingTimeList.map {
                RestTimeModel(it.id, it.history, it.totalTime)
            }
            true
        } catch (e: Exception) {
            failMessage = e.message!!
            false
        }

        return if(result) {
            ResultModel(0, "success", restTimeModelList)
        } else {
            ResultModel(1, failMessage, restTimeModelList)
        }
    }
}
