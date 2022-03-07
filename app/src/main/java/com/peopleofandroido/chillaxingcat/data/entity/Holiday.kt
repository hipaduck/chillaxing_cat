package com.peopleofandroido.chillaxingcat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "holiday_data")
data class Holiday (
    @PrimaryKey(autoGenerate = false) var id: Int,
    @ColumnInfo(name = "name") val name: String
)