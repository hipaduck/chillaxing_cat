package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.chillaxingcat.domain.model.RestTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestTimeRepository

class GetRestTime(
    private val restTimeRepository: RestTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(id: Int): Result<RestTimeModel> {
        val result: ResultModel<RestTimeModel>
        try {
            result = restTimeRepository.getRestingTime(id)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }

        return if (result.code == 0) {
            resultHandler.handleSuccess(result.data!!)
        } else {
            resultHandler.handleFailure(result.message)
        }
    }
}