package com.peopleofandroido.chillaxingcat.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peopleofandroido.chillaxingcat.data.entity.DayOff
import com.peopleofandroido.chillaxingcat.data.entity.Holiday
import com.peopleofandroido.chillaxingcat.domain.model.DateModel

@Dao
interface DayOffDao {
    @Query("SELECT * FROM day_off_data WHERE id = :id")
    fun getDayOff(id: Int) : List<DateModel>

    @Insert(onConflict = REPLACE)
    fun insert(dayOff: DayOff)


    @Delete
    fun delete(dayOff: DayOff)

    companion object {
        internal fun fromDomainModel(dateModel: DateModel) : DayOff {
            return DayOff(
                id = dateModel.id,
                name = dateModel.name
            )
        }
    }
}