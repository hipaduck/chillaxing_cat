package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.repository.HolidayRepository

class AddHoliday(
    private val holidayRepository: HolidayRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(holidayModel: DateModel): Result<String> {
        val addHolidayResult: ResultModel<String>
        try {
            addHolidayResult = holidayRepository.addHoliday(holidayModel)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }
        return if (addHolidayResult.code == 0) {
            return resultHandler.handleSuccess(addHolidayResult.data!!)
        } else {
            Result.error(addHolidayResult.message, null)
        }
    }
}