package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.base.domain.ResultHandler
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.base.domain.Result
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository

class FindOutRestingDaysInMonth(
    private val restingTimeRepository: RestingTimeRepository,
    private val resultHandler: ResultHandler
) {
    suspend operator fun invoke(id: Int): Result<List<Int>> {
        val result: ResultModel<List<Int>>
        try {
            restingTimeRepository.getRestingTime(id) // suspend 유지를 위해 임시 보관

            val mockList = mutableListOf<Int>()
            mockList.add("${id}02".toInt())
            mockList.add("${id}04".toInt())
            mockList.add("${id}07".toInt())
            mockList.add("${id}12".toInt())
            mockList.add("${id}15".toInt())
            mockList.add("${id}20".toInt())
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