package com.hipaduck.chillaxingcat.domain.usecase

import com.hipaduck.base.domain.Result
import com.hipaduck.base.domain.ResultHandler
import com.hipaduck.chillaxingcat.domain.repository.TodayDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetTodayHistory(
    private val repository: TodayDataRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(): Result<String> {
        val result: Flow<String>
        try {
            result = repository.getTodayHistory()
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }

        return try {
            resultHandler.handleSuccess(result.first())
        } catch (e: Exception) {
            resultHandler.handleFailure(e)
        }
    }
}