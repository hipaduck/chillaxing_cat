package com.hipaduck.chillaxingcat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hipaduck.chillaxingcat.domain.model.RestTimeModel

@Entity(tableName = "rest_time")
data class RestTime(
    @PrimaryKey(autoGenerate = false) var id : Int,
    @ColumnInfo(name = "history") val history : String,
    @ColumnInfo(name = "total_time") val totalTime : Long
) {
    companion object {
        internal fun fromDomainModel(restTimeModel: RestTimeModel): RestTime {
            return RestTime(
                id       = restTimeModel.id,
                history = restTimeModel.history,
                totalTime = restTimeModel.totalTime
            )
        }
    }
}