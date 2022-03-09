package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.repository.DayOffRepository

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