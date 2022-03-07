package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository

class AddRestingTime(private val restingTimeRepository: RestingTimeRepository) {
    suspend operator fun invoke(restingTimeModel: RestingTimeModel) = restingTimeRepository.addRestingTime(restingTimeModel)
}