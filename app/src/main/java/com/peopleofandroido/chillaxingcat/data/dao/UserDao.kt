package com.peopleofandroido.chillaxingcat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy.REPLACE
import com.peopleofandroido.chillaxingcat.data.entity.User
import com.peopleofandroido.chillaxingcat.domain.model.UserInfoModel

@Dao
interface UserDao { //TODO : LocalDatabase와 databaseModule 생성 필요
    @Query("SELECT * FROM user")
    fun getAll() : List<User> //User는 단일 개념

    @Insert(onConflict = REPLACE)
    fun insert(user: User)

    @Query("DELETE from user")
    fun deleteAll()

    companion object {
        // usage : val pictureHistory = PictureHistory.fromDomainModel(album)
        internal fun fromDomainModel(userInfoModel: UserInfoModel): User {
            return User(
                id       = userInfoModel.userId,
                userName = userInfoModel.userName,
                userType = userInfoModel.userType
            )
        }
    }
}