package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.data.dao.DayOffDao
import com.peopleofandroido.chillaxingcat.data.entity.DayOff
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.repository.DayOffRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DayOffRepositoryImpl(private val dayOffDao: DayOffDao): DayOffRepository {
    override suspend fun addDayOff(dateModel: DateModel) : ResultModel<String> {
        dayOffDao.insert(DayOff.fromDomainModel(dateModel))

        return ResultModel(0, "success", "success")
    }

    override suspend fun getDayOff(id: Int): ResultModel<DateModel> {
        val dayOff = dayOffDao.getDayOff(id)
        val dateModel = DateModel(dayOff.id, dayOff.name)

        return ResultModel(0, "success", dateModel)
    }

    override suspend fun removeDayOff(dateModel: DateModel): ResultModel<String> {
        dayOffDao.delete(DayOff.fromDomainModel(dateModel))
        return ResultModel(0, "success", "success")
    }
}