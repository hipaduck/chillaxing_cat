package com.hipaduck.chillaxingcat.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.hipaduck.chillaxingcat.data.entity.DayOff

@Dao
interface DayOffDao {
    @Query("SELECT * FROM day_off WHERE id = :id")
    fun getDayOff(id: Int) : DayOff

    @Insert(onConflict = REPLACE)
    fun insert(dayOff: DayOff)

    @Query("SELECT * FROM holiday WHERE id >= :low AND id <= :high")
    fun getDayOffWithPeriod(low: Int, high: Int): List<DayOff>

    @Delete
    fun delete(dayOff: DayOff)
}