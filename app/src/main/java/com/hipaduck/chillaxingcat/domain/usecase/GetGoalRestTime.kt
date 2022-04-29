package com.hipaduck.chillaxingcat.domain.usecase

import com.hipaduck.base.domain.Result
import com.hipaduck.base.domain.ResultHandler
import com.hipaduck.chillaxingcat.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.lang.Exception

class GetGoalRestTime(
    private val repository: SettingsRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(): Result<String> {
        val result: Flow<String>
        try {
            result = repository.getGoalRestTime()
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