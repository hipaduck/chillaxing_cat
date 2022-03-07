package com.peopleofandroido.chillaxingcat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "day_off_data")
data class DayOff (
    @PrimaryKey(autoGenerate = false) var id: Int,
    @ColumnInfo(name = "name") val name: String
)