package com.hipaduck.chillaxingcat.domain.usecase

import com.hipaduck.base.domain.Result
import com.hipaduck.base.domain.ResultHandler
import com.hipaduck.base.domain.model.ResultModel
import com.hipaduck.chillaxingcat.domain.model.RestTimeModel
import com.hipaduck.chillaxingcat.domain.repository.RestTimeRepository

class WriteRestTotalTime(
    private val restTimeRepository: RestTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(id: Int, totalTime: Long) : Result<Boolean> {
        val getRestTimeResult: ResultModel<RestTimeModel>
        try {
            getRestTimeResult = restTimeRepository.getRestingTime(id)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }

        if (getRestTimeResult.code == 0 && getRestTimeResult.data != null) {
            val editRestingTimeResult: ResultModel<Boolean>
            try {
                editRestingTimeResult = restTimeRepository.editTotalTime(id, totalTime)
            } catch (e: Exception) {
                e.printStackTrace()
                return resultHandler.handleFailure(e)
            }

            return if (editRestingTimeResult.code == 0) {
                resultHandler.handleSuccess(editRestingTimeResult.data!!)
            } else {
                resultHandler.handleFailure(editRestingTimeResult.message)
            }

        } else {
            return addChillaxingTime(id, totalTime)
        }
    }

    private suspend fun addChillaxingTime(id: Int, totalTime: Long): Result<Boolean> {
        val addRestingTimeResult: ResultModel<String>
        val restingTimeModel = RestTimeModel(id, "", totalTime)
        try {
            addRestingTimeResult = restTimeRepository.addRestingTime(restingTimeModel)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }
        return if (addRestingTimeResult.code == 0) {
            resultHandler.handleSuccess(true)
        } else {
            resultHandler.handleFailure(addRestingTimeResult.message)
        }
    }
}