package com.peopleofandroido.chillaxingcat.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.peopleofandroido.chillaxingcat.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.prefs.Preferences

val Context.settingsDataStore: DataStore<Settings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer
)

class SettingsDataStore(val context: Context) {
    private val settingsFlow: Flow<Settings> = context.settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(Settings.getDefaultInstance())
            } else {
                throw exception
            }
        }

    val notificationEnabledFlow: Flow<Boolean> = settingsFlow
        .map { settings ->
            settings.notificationEnabled
        }

    val reminderTextFlow: Flow<String> = settingsFlow
        .map { settings ->
            settings.reminderText
        }

    val reminderTimeFlow: Flow<String> = settingsFlow
        .map { settings ->
            settings.reminderTime
        }

    val goalRestingTimeFlow: Flow<String> = settingsFlow
        .map { settings ->
            settings.goalRestingTime
        }

    suspend fun writeNotificationEnabled(status: Boolean) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setNotificationEnabled(status)
                .build()
        }
    }

    suspend fun writeReminderText(text: String) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setReminderText(text)
                .build()
        }
    }

    suspend fun writeGoalRestingTime(time: String) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setGoalRestingTime(time)
                .build()
        }
    }

    suspend fun writeReminderTime(time: String) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setReminderTime(time)
                .build()
        }
    }
}