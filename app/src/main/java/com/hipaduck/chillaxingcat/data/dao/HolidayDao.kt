package com.hipaduck.chillaxingcat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.hipaduck.chillaxingcat.data.entity.Holiday

@Dao
interface HolidayDao {
    @Query("SELECT * FROM holiday WHERE id = :id")
    fun getHoliday(id: Int) : Holiday

    @Query("SELECT * FROM holiday WHERE id LIKE :periodId || '__'")
    fun getHolidayWithPeriod(periodId: String): List<Holiday>

    @Insert(onConflict = REPLACE)
    fun insert(holiday: Holiday)

    @Insert(onConflict = REPLACE)
    fun insertAll(holidayList: List<Holiday>)
}