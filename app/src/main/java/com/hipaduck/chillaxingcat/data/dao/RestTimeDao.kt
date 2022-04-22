package com.hipaduck.chillaxingcat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.hipaduck.chillaxingcat.data.entity.RestTime

@Dao
interface RestTimeDao {
    @Query("SELECT * FROM rest_time WHERE id = :id")
    fun getRestingTime(id: Int) : RestTime

    @Insert(onConflict = REPLACE)
    fun insert(restingTime: RestTime)

    @Query("UPDATE rest_time SET history = :history WHERE id = :id")
    fun editRestingTime(id: Int, history: String)

    @Query("UPDATE rest_time SET total_time = :totalTime WHERE id = :id")
    fun updateTotalTime(id: Int, totalTime: Long): Int

    @Query("SELECT * FROM rest_time WHERE id LIKE :month || '%'")
    fun getRestingDaysInMonth(month: String): List<RestTime>
}