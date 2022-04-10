package com.peopleofandroido.chillaxingcat.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.peopleofandroido.chillaxingcat.TodayData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.todayDataStore: DataStore<TodayData> by dataStore(
    fileName = "todayData.pb",
    serializer = TodayDataSerializer
)

class TodayDataStore(val context: Context) {
    private val todayDataFlow: Flow<TodayData> = context.todayDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(TodayData.getDefaultInstance())
            } else {
                throw exception
            }
        }

    val todayDateFlow: Flow<String> = todayDataFlow
        .map { todayData ->
            todayData.todayDate
        }

    val todayStatusFlow: Flow<String> = todayDataFlow
        .map { todayData ->
            todayData.todayStatus
        }

    val todayHistoryFlow: Flow<String> = todayDataFlow
        .map { todayData ->
            todayData.todayHistory
        }

    suspend fun writeTodayDate(date: String) {
        context.todayDataStore.updateData { todayData ->
            todayData.toBuilder()
                .setTodayDate(date)
                .build()
        }
    }

    suspend fun writeTodayStatus(status: String) {
        context.todayDataStore.updateData { todayData ->
            todayData.toBuilder()
                .setTodayStatus(status)
                .build()
        }
    }

    suspend fun writeTodayHistory(history: String) {
        context.todayDataStore.updateData { todayData ->
            todayData.toBuilder()
                .setTodayHistory(history)
                .build()
        }
    }
}