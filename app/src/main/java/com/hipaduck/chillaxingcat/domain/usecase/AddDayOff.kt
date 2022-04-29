package com.hipaduck.chillaxingcat.domain.usecase

import com.hipaduck.base.domain.model.ResultModel
import com.hipaduck.base.domain.Result
import com.hipaduck.base.domain.ResultHandler
import com.hipaduck.chillaxingcat.domain.model.DateModel
import com.hipaduck.chillaxingcat.domain.repository.DayOffRepository

class AddDayOff(
    private val dayOffRepository: DayOffRepository,
    private val resultHandler: ResultHandler
    ) {
    suspend operator fun invoke(dayOffModel: DateModel): Result<String> {
        val addDayOffResult: ResultModel<String>
        try {
            addDayOffResult = dayOffRepository.addDayOff(dayOffModel)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }
        return if (addDayOffResult.code == 0) {
            resultHandler.handleSuccess(addDayOffResult.data!!)
        } else {
            resultHandler.handleFailure(addDayOffResult.message)
        }
    }
}