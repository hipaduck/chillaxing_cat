package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.chillaxingcat.data.dao.DayOffDao
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.repository.DayOffRepository

class DayOffRepositoryImpl(private val dayOffDao: DayOffDao): DayOffRepository {
    override suspend fun addDayOff(dateModel: DateModel) {
        dayOffDao.insert(DayOffDao.fromDomainModel(dateModel))
    }

    override suspend fun getDayOff(id: Int): List<DateModel> {
        return dayOffDao.getDayOff(id)
    }

    override suspend fun removeDayOff(dateModel: DateModel) {
        dayOffDao.delete(DayOffDao.fromDomainModel(dateModel))
    }
}