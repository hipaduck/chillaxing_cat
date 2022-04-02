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

    val pushAvailableFlow: Flow<Boolean> = settingsFlow
        .map { settings ->
            settings.pushAvailable
        }

    val reminderTextFlow: Flow<String> = settingsFlow
        .map { settings ->
            settings.reminderText
        }

    val reminderTimeFlow: Flow<String> = settingsFlow
        .map { settings ->
            settings.reminderTime
        }

    val goalRestingTimeHourFlow: Flow<Int> = settingsFlow
        .map { settings ->
            settings.goalRestingTimeHour
        }

    val goalRestingTimeMinuteFlow: Flow<Int> = settingsFlow
        .map { settings ->
            settings.goalRestingTimeMinute
        }

    suspend fun writeNotificationEnabled(status: Boolean) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setPushAvailable(status)
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

    suspend fun writeGoalRestingTimeHour(hour: Int) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setGoalRestingTimeHour(hour)
                .build()
        }
    }

    suspend fun writeGoalRestingTimeMinute(minute: Int) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setGoalRestingTimeMinute(minute)
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