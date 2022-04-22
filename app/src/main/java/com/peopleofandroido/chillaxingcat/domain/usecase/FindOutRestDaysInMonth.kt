package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.domain.model.RestTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestTimeRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FindOutRestDaysInMonth(
    private val restTimeRepository: RestTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(startMonth: Int, endMonth: Int): Result<List<RestTimeModel>> {
        val result: ResultModel<List<RestTimeModel>>
        try {
            val startDate = LocalDate.parse("${startMonth}01", DateTimeFormatter.ofPattern("yyyyMMdd"))
            val endDate = LocalDate.parse("${endMonth}01", DateTimeFormatter.ofPattern("yyyyMMdd"))
            var loopDate = startDate
            val resultList = mutableListOf<RestTimeModel>()
            while (loopDate <= endDate) {
                logd("loopDate: $loopDate")
                // 쉰 기록이 남아있는 날짜만 얻어와야 한다.
                val resultModel = restTimeRepository.getRestingTimeInMonth(loopDate.format(DateTimeFormatter.ofPattern("yyyyMM")))
                if (resultModel.code == 0) {
                    resultModel.data?.let {
                        resultList.addAll(it)
                    }
                }
                loopDate = loopDate.plusMonths(1)
            }

            result = ResultModel(0, "success", resultList)
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