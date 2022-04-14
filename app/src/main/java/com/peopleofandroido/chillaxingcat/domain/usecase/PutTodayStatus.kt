package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.chillaxingcat.domain.repository.TodayDataRepository

class PutTodayStatus(
    private val repository: TodayDataRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(status: String): Result<Boolean> {
        try {
            repository.updateTodayStatus(status)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }

        return try {
            resultHandler.handleSuccess(true)
        } catch (e: Exception) {
            resultHandler.handleFailure(e)
        }
    }
}