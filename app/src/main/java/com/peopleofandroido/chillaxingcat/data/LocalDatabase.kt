package com.peopleofandroido.chillaxingcat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.peopleofandroido.chillaxingcat.data.dao.DayOffDao
import com.peopleofandroido.chillaxingcat.data.dao.HolidayDao
import com.peopleofandroido.chillaxingcat.data.dao.RestingTimeDao
import com.peopleofandroido.chillaxingcat.data.entity.DayOff
import com.peopleofandroido.chillaxingcat.data.entity.Holiday
import com.peopleofandroido.chillaxingcat.data.entity.RestingTime

const val DB_VERSION = 5
private const val DB_NAME = "chillaxing_cat.db"

@Database(entities = [Holiday::class, RestingTime::class, DayOff::class], version = DB_VERSION, exportSchema = true)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getHolidayDao(): HolidayDao
    abstract fun getDayOffDao(): DayOffDao
    abstract fun getRestingTimeDao(): RestingTimeDao

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