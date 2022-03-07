package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.repository.DayOffRepository

class RemoveDayOff(private val dayOffRepository: DayOffRepository) {
    suspend operator fun invoke(dayOffModel: DateModel) = dayOffRepository.removeDayOff(dayOffModel)
}