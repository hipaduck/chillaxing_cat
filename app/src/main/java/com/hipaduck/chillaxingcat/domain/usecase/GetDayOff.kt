package com.hipaduck.chillaxingcat.domain.usecase

import com.hipaduck.base.domain.ResultHandler
import com.hipaduck.base.domain.model.ResultModel
import com.hipaduck.base.domain.Result
import com.hipaduck.chillaxingcat.domain.model.DateModel
import com.hipaduck.chillaxingcat.domain.repository.DayOffRepository

class GetDayOff(
    private val dayOffRepository: DayOffRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(id: Int): Result<DateModel> {
        val result: ResultModel<DateModel>
        try {
            result = dayOffRepository.getDayOff(id)
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