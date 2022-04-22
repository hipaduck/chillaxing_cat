package com.peopleofandroido.chillaxingcat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.peopleofandroido.chillaxingcat.data.dao.DayOffDao
import com.peopleofandroido.chillaxingcat.data.dao.HolidayDao
import com.peopleofandroido.chillaxingcat.data.dao.RestTimeDao
import com.peopleofandroido.chillaxingcat.data.entity.DayOff
import com.peopleofandroido.chillaxingcat.data.entity.Holiday
import com.peopleofandroido.chillaxingcat.data.entity.RestTime

const val DB_VERSION = 1
private const val DB_NAME = "chillaxing_cat_test.db" // todo db이름은 향후에 제대로 변경 필요

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