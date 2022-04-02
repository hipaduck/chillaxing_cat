package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.chillaxingcat.domain.repository.SettingsRepository

class PutGoalRestingTimeHour(
    private val repository: SettingsRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(hour: Int): Result<Boolean> {
        try {
            repository.updateGoalRestingTimeHour(hour)
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