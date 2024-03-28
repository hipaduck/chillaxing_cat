package com.hipaduck.chillaxingcat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hipaduck.chillaxingcat.domain.model.DateModel

@Entity(tableName= "holiday")
data class Holiday (
    @PrimaryKey(autoGenerate = false) var id: Int,
    @ColumnInfo(name = "name") val name: String
) {
    companion object {
        internal fun fromDomainModel(dateModel: DateModel) : Holiday {
            return Holiday(
                id = dateModel.id,
                name = dateModel.name
            )
        }
    }
}