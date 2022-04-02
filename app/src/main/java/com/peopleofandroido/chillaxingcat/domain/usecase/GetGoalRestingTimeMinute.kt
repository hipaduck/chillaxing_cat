package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.chillaxingcat.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.lang.Exception

class GetGoalRestingTimeMinute (
    private val repository: SettingsRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(): Result<Int> {
        val result: Flow<Int>
        try {
            result = repository.getGoalRestingTimeMinute()
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