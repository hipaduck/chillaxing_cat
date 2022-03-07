package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.chillaxingcat.domain.repository.DayOffRepository

class GetDayOff(private val dayOffRepository: DayOffRepository) {
    suspend operator fun invoke(id: Int) = dayOffRepository.getDayOff(id)
}