package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.chillaxingcat.data.local.SettingsDataStore
import com.peopleofandroido.chillaxingcat.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

internal class SettingsRepositoryImpl(
    private val settingsDataStore: SettingsDataStore,
): SettingsRepository {
    override suspend fun getNotificationEnabledStatus(): Flow<Boolean> = settingsDataStore.notificationEnabledFlow

    override suspend fun updateNotificationEnabledStatus(status: Boolean) = settingsDataStore.writeNotificationEnabled(status)
}