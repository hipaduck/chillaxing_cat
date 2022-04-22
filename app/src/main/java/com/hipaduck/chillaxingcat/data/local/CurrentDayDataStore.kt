package com.hipaduck.chillaxingcat.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.hipaduck.chillaxingcat.CurrentDayData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.todayDataStore: DataStore<CurrentDayData> by dataStore(
    fileName = "currentDayData.pb",
    serializer = CurrentDayDataSerializer
)

class TodayDataStore(val context: Context) {
    private val todayDataFlow: Flow<CurrentDayData> = context.todayDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(CurrentDayData.getDefaultInstance())
            } else {
                throw exception
            }
        }

    val todayDateFlow: Flow<String> = todayDataFlow
        .map { todayData ->
            todayData.currentDayDate
        }

    val todayStatusFlow: Flow<String> = todayDataFlow
        .map { todayData ->
            todayData.currentDayStatus
        }

    val todayHistoryFlow: Flow<String> = todayDataFlow
        .map { todayData ->
            todayData.currentDayHistory
        }

    suspend fun writeTodayDate(date: String) {
        context.todayDataStore.updateData { todayData ->
            todayData.toBuilder()
                .setCurrentDayDate(date)
                .build()
        }
    }

    suspend fun writeTodayStatus(status: String) {
        context.todayDataStore.updateData { todayData ->
            todayData.toBuilder()
                .setCurrentDayStatus(status)
                .build()
        }
    }

    suspend fun writeTodayHistory(history: String) {
        context.todayDataStore.updateData { todayData ->
            todayData.toBuilder()
                .setCurrentDayHistory(history)
                .build()
        }
    }
}