package com.peopleofandroido.chillaxingcat.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy.REPLACE
import com.peopleofandroido.chillaxingcat.data.entity.RestingTime
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel

@Dao
interface RestingTimeDao {
    @Query("SELECT * FROM resting_time WHERE id = :id")
    fun getRestingTime(id: Int) : List<RestingTimeModel>

    @Insert(onConflict = REPLACE)
    fun insert(restingTime: RestingTime)

    @Query("UPDATE resting_time SET history = :history WHERE id = :id")
    fun editRestingTime(id: Int, history: String)

    companion object {
        internal fun fromDomainModel(restingTimeModel: RestingTimeModel): RestingTime {
            return RestingTime(
                id       = restingTimeModel.id,
                history = restingTimeModel.history,
                totalTime = restingTimeModel.totalTime
            )
        }
    }
}