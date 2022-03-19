package com.peopleofandroido.chillaxingcat.domain.model

import com.peopleofandroido.chillaxingcat.data.entity.Holiday

data class DateModel (
    var id: Int,
    var name: String,
) {
    companion object {
        internal fun fromHoliday(holiday: Holiday): DateModel {
            return DateModel(
                id = holiday.id,
                name = holiday.name
            )
        }
    }
}