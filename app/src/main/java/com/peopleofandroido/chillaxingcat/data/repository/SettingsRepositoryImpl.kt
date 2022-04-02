package com.peopleofandroido.chillaxingcat.data.repository

import com.peopleofandroido.chillaxingcat.data.local.SettingsDataStore
import com.peopleofandroido.chillaxingcat.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

internal class SettingsRepositoryImpl(
    private val settingsDataStore: SettingsDataStore,
): SettingsRepository {
    override suspend fun getNotificationEnabledStatus(): Flow<Boolean> = settingsDataStore.pushAvailableFlow
    override suspend fun updateNotificationEnabledStatus(status: Boolean) = settingsDataStore.writeNotificationEnabled(status)

    override suspend fun getReminderText(): Flow<String> = settingsDataStore.reminderTextFlow
    override suspend fun updateReminderText(text: String) = settingsDataStore.writeReminderText(text)

    override suspend fun getReminderTime(): Flow<String> = settingsDataStore.reminderTimeFlow
    override suspend fun updateReminderTime(time: String) = settingsDataStore.writeReminderTime(time)

    override suspend fun getGoalRestingTimeHour(): Flow<Int> = settingsDataStore.goalRestingTimeHourFlow
    override suspend fun updateGoalRestingTimeHour(hour: Int) = settingsDataStore.writeGoalRestingTimeHour(hour)

    override suspend fun getGoalRestingTimeMinute(): Flow<Int> = settingsDataStore.goalRestingTimeMinuteFlow
    override suspend fun updateGoalRestingTimeMinute(minute: Int) = settingsDataStore.writeGoalRestingTimeMinute(minute)
}