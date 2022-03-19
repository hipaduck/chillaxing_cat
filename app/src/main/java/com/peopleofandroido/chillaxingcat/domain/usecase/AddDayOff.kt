package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.repository.DayOffRepository

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