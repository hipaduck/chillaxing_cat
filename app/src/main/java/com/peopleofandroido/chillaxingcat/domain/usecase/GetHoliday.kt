package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.DayInfoList
import com.peopleofandroido.chillaxingcat.domain.model.YearMonth
import com.peopleofandroido.chillaxingcat.domain.repository.FreeDayRepository
import kotlin.Exception

class GetHoliday(
    private val repository: FreeDayRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(yearMonth: YearMonth): Result<DayInfoList> {
        val result: ResultModel<DayInfoList>
        try {
            result = repository.getHolidaysInMonth(yearMonth.yearStr, yearMonth.monthStr)
        } catch (e: Exception) {
            e.printStackTrace()
            return resultHandler.handleFailure(e)
        }

        return try {
            if (result.code == 0) {
                result.data?.let {
                    resultHandler.handleSuccess(it)
                } ?: run {
                    resultHandler.handleFailure("result data is null")
                }
            } else {
                resultHandler.handleFailure(result.message)
            }
        } catch (e: Exception) {
            resultHandler.handleFailure(e)
        }
    }
}