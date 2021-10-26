package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.chillaxingcat.data.dao.UserDao
import com.peopleofandroido.chillaxingcat.data.entity.User
import com.peopleofandroido.chillaxingcat.domain.model.UserInfoModel
import com.peopleofandroido.chillaxingcat.domain.repository.UserInfoRepository

class UserInfoRepositoryImpl(private val userDao: UserDao) : UserInfoRepository {
    override suspend fun addUserInfo(userInfo: UserInfoModel)
        = userDao.insert(UserDao.fromDomainModel(userInfo))

    override suspend fun modifyUserInfo(userInfo: UserInfoModel) {
        userDao.deleteAll()
        addUserInfo(userInfo)
    }

    override suspend fun getUserInfo() : List<User> {
        return userDao.getAll()
    }

}
