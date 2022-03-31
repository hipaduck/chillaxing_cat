package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.chillaxingcat.domain.repository.SettingsRepository

class PutNotificationStatus(
    private val repository: SettingsRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(status: Boolean): Result<Boolean> {
        try {
            repository.updateNotificationEnabledStatus(status)
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