package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.DayInfoList
import com.peopleofandroido.chillaxingcat.domain.model.Period
import com.peopleofandroido.chillaxingcat.domain.repository.HolidayRepository
import kotlin.Exception

class GetHolidayWithPeriod( //TODO : db에 없으면 서버 조회하도록 수정 필요하나, 어떻게 할 것인가?
    private val repository: HolidayRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(period: Period): Result<DayInfoList> {
        val result: ResultModel<DayInfoList>
        try {
            result = repository.requestHolidayWithPeriod(period.yearStr, period.monthStr)
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