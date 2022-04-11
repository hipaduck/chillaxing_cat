package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.repository.HolidayRepository
import kotlin.Exception

class GetHolidayWithPeriod(
    private val repository: HolidayRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(startPeriod: String, endPeriod: String): Result<List<DateModel>> {
        var result: ResultModel<List<DateModel>>
        try {
//            result = repository.getHolidayWithPeriod(startPeriod, endPeriod)

            // todo below testcode will be removed
            val mockList = mutableListOf<DateModel>()
            mockList.add(DateModel("20220301".toInt(),"삼일절"))
            mockList.add(DateModel("20220405".toInt(), "식목일"))
            mockList.add(DateModel("20220505".toInt(), "어린이날"))
            result = ResultModel(0, "success", mockList)

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