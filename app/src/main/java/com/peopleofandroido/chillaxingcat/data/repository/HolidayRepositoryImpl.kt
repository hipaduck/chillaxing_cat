package com.peopleofandroido.chillaxingcat.data.repository

import com.google.gson.Gson
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.base.util.getErrorResultModel
import com.peopleofandroido.chillaxingcat.data.dao.HolidayDao
import com.peopleofandroido.chillaxingcat.data.entity.Holiday
import com.peopleofandroido.chillaxingcat.data.remote.api.HolidayApi
import com.peopleofandroido.chillaxingcat.data.remote.model.toDomainModel
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.model.DayInfoList
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.HolidayRepository
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class HolidayRepositoryImpl(
    private val holidayDao: HolidayDao,
    private val holidayApi: HolidayApi,
    private val gson: Gson
) : HolidayRepository {
    private val authKey = "H4x622Qm0l4CRmP3aW++k0EZw++gIW0xnawVEKxNDWCKRg8HjZjyyQQnrEn8cToDhZkHiHxYDkvAAcMOohqbbA=="

    override suspend fun requestHolidayWithPeriod(year: String, month: String): ResultModel<DayInfoList> {
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

    override suspend fun addHoliday(dateModel: DateModel): ResultModel<String> {
        var failMessage = ""
        val result: Boolean = suspendCoroutine { cont->
            try {
                holidayDao.insert(Holiday.fromDomainModel(dateModel))
                cont.resume(true)
            } catch (e: Exception) {
                failMessage = e.message!!
                cont.resume(false)
            }
        }
        return if (result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "fail adding Holiday $dateModel")
        }
    }

    override suspend fun getHoliday(id: Int): ResultModel<DateModel> {
        var dateModel : DateModel? = null

        var failMessage = ""
        val result: Boolean = suspendCoroutine { cont->
            try {
                val holiday = holidayDao.getHoliday(id)
                dateModel = DateModel(holiday.id, holiday.name)
                cont.resume(true)
            } catch (e: Exception) {
                failMessage = e.message!!
                cont.resume(false)
            }
        }

        return if (result) {
            ResultModel(0, "success", dateModel)
        } else {
            ResultModel(1, failMessage, dateModel)
        }
    }
}