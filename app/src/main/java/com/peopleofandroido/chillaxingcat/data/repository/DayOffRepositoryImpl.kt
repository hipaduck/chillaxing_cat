package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.data.dao.DayOffDao
import com.peopleofandroido.chillaxingcat.data.entity.DayOff
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.model.Period
import com.peopleofandroido.chillaxingcat.domain.repository.DayOffRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DayOffRepositoryImpl(private val dayOffDao: DayOffDao): DayOffRepository {
    override suspend fun addDayOff(dateModel: DateModel) : ResultModel<String> {
        var failMessage = ""
        val result : Boolean = suspendCoroutine { cont ->
            try {
                dayOffDao.insert(DayOff.fromDomainModel(dateModel))
                cont.resume(true)
            } catch (e: Exception) {
                failMessage = e.message!!
                cont.resume(false)
            }
        }

        return if(result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "fail")
        }

    }

    override suspend fun getDayOff(id: Int): ResultModel<DateModel> {
        var dateModel : DateModel? = null

        var failMessage = ""
        val result: Boolean = suspendCoroutine { cont->
            try {
                val dayOff = dayOffDao.getDayOff(id)
                dateModel = DateModel(dayOff.id, dayOff.name)
                cont.resume(true)
            } catch (e: java.lang.Exception) {
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

    override suspend fun removeDayOff(dateModel: DateModel): ResultModel<String> {
        var failMessage = ""
        val result: Boolean = suspendCoroutine { cont->
            try {
                dayOffDao.delete(DayOff.fromDomainModel(dateModel))
                cont.resume(true)
            } catch (e: java.lang.Exception) {
                failMessage = e.message!!
                cont.resume(false)
            }
        }
        return if (result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "fail removing DayOff $dateModel")
        }
    }

    override suspend fun getDayOffWithPeriod(
        startPeriod: Period,
        endPeriod: Period
    ): ResultModel<List<DateModel>> {
        var dayOffList : List<DayOff>? = null

        var failMessage = ""
        val result: Boolean = suspendCoroutine { cont->
            try {
                dayOffList = dayOffDao.getDayOffWithPeriod(startPeriod.yearMonthId, endPeriod.yearMonthId)
                cont.resume(true)
            } catch (e: java.lang.Exception) {
                failMessage = e.message!!
                cont.resume(false)
            }
        }

        val dateModelList : ArrayList<DateModel> = arrayListOf()
        dayOffList?.let { list ->
            for(dayOff in list) {
                dateModelList.add(DateModel(dayOff.id, dayOff.name))
            }
        }

        return if (result) {
            ResultModel(0, "success", dateModelList)
        } else {
            ResultModel(1, failMessage, dateModelList)
        }
    }

}