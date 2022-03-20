package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository

class AddRestingTime(
    private val restingTimeRepository: RestingTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(restingTimeModel: RestingTimeModel) : Result<String> {
        val addRestingTimeResult: ResultModel<String>
        try {
            addRestingTimeResult = restingTimeRepository.addRestingTime(restingTimeModel)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }
        return if (addRestingTimeResult.code == 0) {
            resultHandler.handleSuccess(addRestingTimeResult.data!!)
        } else {
            resultHandler.handleFailure(addRestingTimeResult.message)
        }
    }
}