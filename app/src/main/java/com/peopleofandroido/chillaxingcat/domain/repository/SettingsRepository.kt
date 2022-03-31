package com.peopleofandroido.chillaxingcat.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    // 알림 활성화 상태가 true이면 활성화, false이면 비활성화 상태를 의미
    suspend fun getNotificationEnabledStatus(): Flow<Boolean>
    suspend fun updateNotificationEnabledStatus(status: Boolean)
}