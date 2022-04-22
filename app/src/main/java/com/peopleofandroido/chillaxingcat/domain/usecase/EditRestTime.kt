package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.RestTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestTimeRepository

class EditRestTime(
    private val restTimeRepository: RestTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(id: Int, history: String) : Result<String> {
        val getRestTimeResult: ResultModel<RestTimeModel>
        try {
            getRestTimeResult = restTimeRepository.getRestingTime(id)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }

        if (getRestTimeResult.code == 0 && getRestTimeResult.data != null) {
            val editRestingTimeResult: ResultModel<String>
            try {
                editRestingTimeResult = restTimeRepository.editRestingTime(id, history)
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