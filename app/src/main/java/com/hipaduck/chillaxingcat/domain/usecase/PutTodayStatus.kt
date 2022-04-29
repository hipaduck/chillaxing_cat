package com.hipaduck.chillaxingcat.domain.usecase

import com.hipaduck.base.domain.Result
import com.hipaduck.base.domain.ResultHandler
import com.hipaduck.chillaxingcat.domain.repository.TodayDataRepository

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