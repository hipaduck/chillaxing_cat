package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository

class GetRestingTime(private val restingTimeRepository: RestingTimeRepository) {
    suspend operator fun invoke(id: Int) = restingTimeRepository.getRestingTime(id)
}