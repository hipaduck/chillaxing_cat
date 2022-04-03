package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.chillaxingcat.data.local.SettingsDataStore
import com.peopleofandroido.chillaxingcat.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

internal class SettingsRepositoryImpl(
    private val settingsDataStore: SettingsDataStore,
): SettingsRepository {
    override suspend fun getNotificationEnabledStatus(): Flow<Boolean> = settingsDataStore.pushEnabledFlow
    override suspend fun updateNotificationEnabledStatus(status: Boolean) = settingsDataStore.writeNotificationEnabled(status)

    override suspend fun getReminderText(): Flow<String> = settingsDataStore.reminderTextFlow
    override suspend fun updateReminderText(text: String) = settingsDataStore.writeReminderText(text)

    override suspend fun getReminderTime(): Flow<String> = settingsDataStore.reminderTimeFlow
    override suspend fun updateReminderTime(time: String) = settingsDataStore.writeReminderTime(time)

    override suspend fun getGoalRestingTime(): Flow<String> = settingsDataStore.goalRestingTimeFlow
    override suspend fun updateGoalRestingTime(time: String) = settingsDataStore.writeGoalRestingTime(time)

    override suspend fun getIsAppFirstLaunched(): Flow<Boolean> = settingsDataStore.isAppFirstLaunchedFlow
    override suspend fun updateIsAppFirstLaunched(status: Boolean) = settingsDataStore.writeIsAppFirstLaunched(status)
}