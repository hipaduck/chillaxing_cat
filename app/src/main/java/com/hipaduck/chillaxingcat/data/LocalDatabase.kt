package com.hipaduck.chillaxingcat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hipaduck.chillaxingcat.data.dao.DayOffDao
import com.hipaduck.chillaxingcat.data.dao.HolidayDao
import com.hipaduck.chillaxingcat.data.dao.RestTimeDao
import com.hipaduck.chillaxingcat.data.entity.DayOff
import com.hipaduck.chillaxingcat.data.entity.Holiday
import com.hipaduck.chillaxingcat.data.entity.RestTime

const val DB_VERSION = 1
private const val DB_NAME = "chillaxing_cat.db"

@Database(entities = [Holiday::class, RestTime::class, DayOff::class], version = DB_VERSION, exportSchema = true)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getHolidayDao(): HolidayDao
    abstract fun getDayOffDao(): DayOffDao
    abstract fun getRestingTimeDao(): RestTimeDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also {
                    INSTANCE = it
                }
            }

        private fun build(context: Context) =
            Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, DB_NAME)
                .build()
    }
}