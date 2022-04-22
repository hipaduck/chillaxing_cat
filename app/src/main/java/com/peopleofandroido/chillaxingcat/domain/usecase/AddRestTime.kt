package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.RestTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestTimeRepository

class AddRestTime(
    private val restTimeRepository: RestTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(restTimeModel: RestTimeModel) : Result<String> {
        val addRestingTimeResult: ResultModel<String>
        try {
            addRestingTimeResult = restTimeRepository.addRestingTime(restTimeModel)
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