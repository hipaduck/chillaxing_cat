package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.chillaxingcat.domain.repository.HolidayRepository

class GetHoliday(private val holidayRepository: HolidayRepository) {
    suspend operator fun invoke(id: Int) = holidayRepository.getHoliday(id)
}