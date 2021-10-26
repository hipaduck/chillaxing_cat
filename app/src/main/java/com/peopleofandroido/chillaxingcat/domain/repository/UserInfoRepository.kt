package com.peopleofandroido.chillaxingcat.domain.repository

import com.peopleofandroido.chillaxingcat.data.entity.User
import com.peopleofandroido.chillaxingcat.domain.model.UserInfoModel

interface UserInfoRepository {
    suspend fun addUserInfo(userInfo: UserInfoModel)
    suspend fun modifyUserInfo(userInfo: UserInfoModel)
    suspend fun getUserInfo(): List<User>
}