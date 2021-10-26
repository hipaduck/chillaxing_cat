package com.peopleofandroido.chillaxingcat.domain.repository

import com.peopleofandroido.base.domain.model.ResultModel
import com.peopleofandroido.chillaxingcat.domain.model.DayInfoList
import com.peopleofandroido.chillaxingcat.domain.model.FreeDayModel

interface FreeDayRepository {
    suspend fun insertFreeDaysToLocal(freeDayList: List<FreeDayModel>)
    suspend fun getHolidaysInMonth(year: String, month: String): ResultModel<DayInfoList>
}