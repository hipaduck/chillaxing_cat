package com.hipaduck.chillaxingcat.data.repository

import com.hipaduck.base.domain.model.ResultModel
import com.hipaduck.chillaxingcat.data.dao.DayOffDao
import com.hipaduck.chillaxingcat.data.entity.DayOff
import com.hipaduck.chillaxingcat.domain.model.DateModel
import com.hipaduck.chillaxingcat.domain.repository.DayOffRepository

class DayOffRepositoryImpl(private val dayOffDao: DayOffDao): DayOffRepository {
    override suspend fun addDayOff(dateModel: DateModel) : ResultModel<String> {
        var failMessage = ""

        val result: Boolean = try {
            dayOffDao.insert(DayOff.fromDomainModel(dateModel))
            true
        } catch (e: Exception) {
            failMessage = e.message!!
            false
        }

        return if(result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "while inserting ${dateModel.id.toString()}")
        }
    }

    override suspend fun getDayOff(id: Int): ResultModel<DateModel> {
        var failMessage = ""
        var dayOff: DayOff? = null
        var dateModel: DateModel? = null

        val result: Boolean = try {
            dayOff = dayOffDao.getDayOff(id)
            dateModel = DateModel(dayOff.id, dayOff.name)
            true
        } catch (e: Exception) {
            failMessage = e.message!!
            false
        }

        return if(result) {
            ResultModel(0, "success", dateModel)
        } else {
            ResultModel(1, failMessage, dateModel)
        }
    }

    override suspend fun removeDayOff(dateModel: DateModel): ResultModel<String> {
        var failMessage = ""

        val result: Boolean = try {
            dayOffDao.delete(DayOff.fromDomainModel(dateModel))
            true
        } catch (e: Exception) {
            failMessage = e.message!!
            false
        }

        return if(result) {
            ResultModel(0, "success", "success")
        } else {
            ResultModel(1, failMessage, "while removing ${dateModel.id.toString()}")
        }
    }
}