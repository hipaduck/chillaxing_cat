package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
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
            val startDate = LocalDate.parse(startMonth.toString(), DateTimeFormatter.ofPattern("yyyyMM"))
            val endDate = LocalDate.parse(endMonth.toString(), DateTimeFormatter.ofPattern("yyyyMM"))
            var loopDate = startDate
            while (loopDate <= endDate) {
                restingTimeRepository.getRestingTime(loopDate.format(DateTimeFormatter.ofPattern("yyyyMM")).toInt())
                loopDate = loopDate.plusMonths(1)
            }

//            restingTimeRepository.getRestingTime(startMonth) // suspend 유지를 위해 임시 보관
//            restingTimeRepository.getRestingTime(endMonth) // suspend 유지를 위해 임시 보관

            val mockList = mutableListOf<Int>()
            mockList.add("${startMonth}02".toInt())
            mockList.add("${startMonth}04".toInt())
            mockList.add("${startMonth}07".toInt())
            mockList.add("${endMonth}12".toInt())
            mockList.add("${endMonth}15".toInt())
            mockList.add("${endMonth}20".toInt())
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