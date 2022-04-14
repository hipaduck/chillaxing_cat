package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository

class WriteChillaxingTotalTime(
    private val restingTimeRepository: RestingTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(id: Int, totalTime: Long) : Result<Boolean> {
        val getRestingTimeResult: ResultModel<RestingTimeModel>
        try {
            getRestingTimeResult = restingTimeRepository.getRestingTime(id)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }

        if (getRestingTimeResult.code == 0 && getRestingTimeResult.data != null) {
            val editRestingTimeResult: ResultModel<Boolean>
            try {
                editRestingTimeResult = restingTimeRepository.editTotalTime(id, totalTime)
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
        val restingTimeModel = RestingTimeModel(id, "", totalTime)
        try {
            addRestingTimeResult = restingTimeRepository.addRestingTime(restingTimeModel)
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