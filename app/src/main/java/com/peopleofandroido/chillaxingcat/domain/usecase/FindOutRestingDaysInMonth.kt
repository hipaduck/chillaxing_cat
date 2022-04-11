package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FindOutRestingDaysInMonth(
    private val restingTimeRepository: RestingTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(startMonth: Int, endMonth: Int): Result<List<Int>> {
        val result: ResultModel<List<Int>>
        try {
            val startDate = LocalDate.parse("${startMonth}01", DateTimeFormatter.ofPattern("yyyyMMdd"))
            val endDate = LocalDate.parse("${endMonth}01", DateTimeFormatter.ofPattern("yyyyMMdd"))
            var loopDate = startDate
            while (loopDate <= endDate) {
                logd("loopDate: $loopDate")
                // 쉰 기록이 남아있는 날짜만 얻어와야 한다.
                restingTimeRepository.getRestingTime(loopDate.format(DateTimeFormatter.ofPattern("yyyyMM")).toInt())
                loopDate = loopDate.plusMonths(1)
            }

            val mockList = mutableListOf<Int>()
            mockList.add("20220302".toInt())
            mockList.add("20220321".toInt())
            mockList.add("20220327".toInt())
            mockList.add("20220402".toInt())
            mockList.add("20220407".toInt())
            mockList.add("20220409".toInt())
            mockList.add("20220215".toInt())
            mockList.add("20220201".toInt())
            mockList.add("20220202".toInt())
            mockList.add("20220203".toInt())
            mockList.add("20220218".toInt())
            mockList.add("20220228".toInt())
//            mockList.add("${endMonth}20".toInt())
            result = ResultModel(0, "success", mockList)
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