package com.hipaduck.chillaxingcat.domain.usecase

import com.hipaduck.base.domain.Result
import com.hipaduck.base.domain.ResultHandler
import com.hipaduck.chillaxingcat.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class IsRequiredValuesEntered(
    private val repository: SettingsRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(): Result<Boolean> {
        val result: Flow<String>
        try {
            result = repository.getGoalRestTime()

            // 만약 notification y/n
            // notification n일 경우에는 goal rest time만으로 충분
            // notification y일 경우에는 관련 데이터가 모두 필요
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }

        return try {
            resultHandler.handleSuccess(result.first().isEmpty())
        } catch (e: Exception) {
            resultHandler.handleFailure(e)
        }
    }
}