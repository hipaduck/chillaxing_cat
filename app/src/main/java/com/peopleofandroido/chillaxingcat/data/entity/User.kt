package com.peopleofandroido.chillaxingcat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) var id : Int,
    @ColumnInfo(name = "usertype") val userType : String,
    @ColumnInfo(name = "username")val userName : String
)