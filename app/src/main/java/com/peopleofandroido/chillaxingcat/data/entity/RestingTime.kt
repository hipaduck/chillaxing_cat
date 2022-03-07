package com.peopleofandroido.chillaxingcat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resting_time")
data class RestingTime(
    @PrimaryKey(autoGenerate = false) var id : Int,
    @ColumnInfo(name = "history") val history : String,
    @ColumnInfo(name = "totalTime") val totalTime : String
)