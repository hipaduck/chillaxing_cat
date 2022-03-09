package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository

class EditRestingTime(
    private val restingTimeRepository: RestingTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(id: Int, history: String) : Result<String> {
        val getRestingTimeResult: ResultModel<RestingTimeModel>
        try {
            getRestingTimeResult = restingTimeRepository.getRestingTime(id)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }

        if (getRestingTimeResult.code == 0 && getRestingTimeResult.data != null) {
            val editRestingTimeResult: ResultModel<String>
            try {
                editRestingTimeResult = restingTimeRepository.editRestingTime(id, history)
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
            return resultHandler.handleFailure("there is no matching resting time with $id")
        }
    }
}