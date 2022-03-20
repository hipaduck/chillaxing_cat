package com.peopleofandroido.chillaxingcat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel

@Entity(tableName = "resting_time")
data class RestingTime(
    @PrimaryKey(autoGenerate = false) var id : Int,
    @ColumnInfo(name = "history") val history : String,
    @ColumnInfo(name = "total_time") val totalTime : String
) {
    companion object {
        internal fun fromDomainModel(restingTimeModel: RestingTimeModel): RestingTime {
            return RestingTime(
                id       = restingTimeModel.id,
                history = restingTimeModel.history,
                totalTime = restingTimeModel.totalTime
            )
        }
    }
}