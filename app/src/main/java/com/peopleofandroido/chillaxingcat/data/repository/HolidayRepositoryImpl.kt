package com.peopleofandroido.chillaxingcat.data.repository

import android.util.Log
import com.google.gson.Gson
import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.BuildConfig
import com.peopleofandroido.chillaxingcat.data.dao.HolidayDao
import com.peopleofandroido.chillaxingcat.data.entity.Holiday
import com.peopleofandroido.chillaxingcat.data.remote.api.HolidayApi
import com.peopleofandroido.chillaxingcat.data.remote.model.toDateModel
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.repository.HolidayRepository
import java.text.SimpleDateFormat
import java.util.*

internal class HolidayRepositoryImpl(
    private val holidayDao: HolidayDao,
    private val holidayApi: HolidayApi,
    private val gson: Gson
) : HolidayRepository {

    override suspend fun getHolidayWithPeriod(
        startPeriod: String,
        endPeriod: String
    ): ResultModel<List<DateModel>> {
        val periodIdDateFormat = SimpleDateFormat("yyyyMM")
        val monthDateFormat = SimpleDateFormat("MM")
        val yearDateFormat = SimpleDateFormat("yyyy")

        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()

        startDate.time = periodIdDateFormat.parse(startPeriod)
        endDate.time = periodIdDateFormat.parse(endPeriod)

        val holidayList = arrayListOf<DateModel>()

        while (startDate <= endDate) {
            //db 조회
            val periodId = periodIdDateFormat.format(startDate.time)
            val localList : List<Holiday> = holidayDao.getHolidayWithPeriod(periodId)

            if (localList.isNotEmpty()) {
                for (holiday in localList) {
                    holidayList.add(DateModel.fromHoliday(holiday))
                }
            } else {
                //서버 조회
                val year = yearDateFormat.format(startDate.time)
                val month = monthDateFormat.format(startDate.time)

                val response = holidayApi.requestHoliday(
                    year = year,
                    month = month,
                    BuildConfig.HOLIDAY_SERVER_KEY
                )

                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val responseList = responseBody.response.body.item.toDateModel()
                        val insertList = arrayListOf<Holiday>()
                        for (date in responseList) {
                            holidayList.add(date)
                            insertList.add(Holiday.fromDomainModel(date))
                        }

                        try {
                            holidayDao.insertAll(insertList)
                        } catch (e: Exception) {
                            Log.e("GAEGUL", "getHolidayWithPeriod: adding error ${e.message}")
                        }
                    } ?: run {
                        Log.d("GAEGUL", "getHolidayWithPeriod: response body is null")
                        return ResultModel(1, "request error", arrayListOf())
                    }
                } else {
                    return ResultModel(1, "request error ${response.errorBody()}", arrayListOf())
                }
            }

            startDate.add(Calendar.MONTH, 1)
        }

        return ResultModel(0, "success", holidayList)
    }

    override suspend fun addHoliday(dateModel: DateModel): ResultModel<String> {
        holidayDao.insert(Holiday.fromDomainModel(dateModel))

        return ResultModel(0, "success", "success")
    }

    override suspend fun getHoliday(id: Int): ResultModel<DateModel> {
        val holiday = holidayDao.getHoliday(id)
        val dateModel = DateModel(holiday.id, holiday.name)

        return ResultModel(0, "success", dateModel)
    }


}