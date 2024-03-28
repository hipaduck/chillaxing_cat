package com.hipaduck.chillaxingcat.domain.usecase

import com.hipaduck.base.domain.ResultHandler
import com.hipaduck.base.domain.model.ResultModel
import com.hipaduck.base.domain.Result
import com.hipaduck.chillaxingcat.domain.model.RestTimeModel
import com.hipaduck.chillaxingcat.domain.repository.RestTimeRepository

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