package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.repository.HolidayRepository

class AddHoliday(private val holidayRepository: HolidayRepository) {
    suspend operator fun invoke(holidayModel: DateModel) = holidayRepository.addHoliday(holidayModel)
}