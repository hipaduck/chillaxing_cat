package com.peopleofandroido.chillaxingcat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peopleofandroido.chillaxingcat.data.entity.Holiday
import com.peopleofandroido.chillaxingcat.domain.model.DateModel

@Dao
interface HolidayDao {
    @Query("SELECT * FROM holiday WHERE id = :id")
    fun getHoliday(id: Int) : Holiday

//    @Query("SELECT * FROM holiday WHERE id >= :low AND id <= :high")
//    fun getHolidayWithPeriod(low: Int, high: Int): List<Holiday>

    @Insert(onConflict = REPLACE)
    fun insert(holiday: Holiday)
}