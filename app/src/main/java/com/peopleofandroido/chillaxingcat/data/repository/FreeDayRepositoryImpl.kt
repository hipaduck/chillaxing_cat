package com.peopleofandroido.chillaxingcat.data.repository

import com.google.gson.Gson
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.base.util.getErrorResultModel
import com.peopleofandroido.chillaxingcat.data.remote.api.HolidayApi
import com.peopleofandroido.chillaxingcat.data.remote.model.toDomainModel
import com.peopleofandroido.chillaxingcat.domain.model.DayInfoList
import com.peopleofandroido.chillaxingcat.domain.model.FreeDayModel
import com.peopleofandroido.chillaxingcat.domain.repository.FreeDayRepository

internal class FreeDayRepositoryImpl(
    private val holidayApi: HolidayApi,
    private val gson: Gson
) : FreeDayRepository {
    private val authKey = "H4x622Qm0l4CRmP3aW++k0EZw++gIW0xnawVEKxNDWCKRg8HjZjyyQQnrEn8cToDhZkHiHxYDkvAAcMOohqbbA=="

    override suspend fun insertFreeDaysToLocal(freeDayList: List<FreeDayModel>) {
        TODO("Not yet implemented")
    }

    override suspend fun getHolidaysInMonth(year: String, month: String): ResultModel<DayInfoList> {
        val response = holidayApi.getHoliday(year = year, month = month, authKey)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            responseBody?.let {
                val resultData = it.response.body.item.toDomainModel()

                ResultModel(0, "success", resultData)
            } ?: run {
                ResultModel(1, "Response body is null", null)
            }
        } else {
            response.getErrorResultModel(gson)
        }
    }
}