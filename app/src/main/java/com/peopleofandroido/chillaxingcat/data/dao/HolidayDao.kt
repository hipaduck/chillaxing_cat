package com.peopleofandroido.chillaxingcat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peopleofandroido.chillaxingcat.data.entity.Holiday
import com.peopleofandroido.chillaxingcat.domain.model.DateModel

@Dao
interface HolidayDao {
    @Query("SELECT * FROM holiday_data WHERE id = :id")
    fun getHoliday(id: Int) : List<DateModel>

    @Insert(onConflict = REPLACE)
    fun insert(holiday: Holiday)

    companion object {
        internal fun fromDomainModel(dateModel: DateModel) : Holiday {
            return Holiday(
                id = dateModel.id,
                name = dateModel.name
            )
        }
    }
}