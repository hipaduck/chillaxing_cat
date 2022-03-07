package com.peopleofandroido.chillaxingcat.domain.usecase

import com.peopleofandroido.chillaxingcat.domain.repository.RestingTimeRepository

class EditRestingTime(private val restingTimeRepository: RestingTimeRepository) {
    suspend operator fun invoke(id: Int, history: String) = restingTimeRepository.editRestingTime(id, history)
}