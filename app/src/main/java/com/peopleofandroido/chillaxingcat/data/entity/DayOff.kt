package com.peopleofandroido.chillaxingcat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peopleofandroido.chillaxingcat.domain.model.DateModel

@Entity(tableName= "day_off")
data class DayOff (
    @PrimaryKey(autoGenerate = false) var id: Int,
    @ColumnInfo(name = "name") val name: String
) {
    companion object {
        internal fun fromDomainModel(dateModel: DateModel) : DayOff {
            return DayOff(
                id = dateModel.id,
                name = dateModel.name
            )
        }
    }
}