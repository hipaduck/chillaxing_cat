package com.hipaduck.chillaxingcat.data.repository

import com.hipaduck.chillaxingcat.data.local.TodayDataStore
import com.hipaduck.chillaxingcat.domain.repository.TodayDataRepository
import kotlinx.coroutines.flow.Flow

internal class TodayDataRepositoryImpl(
    private val todayDataStore: TodayDataStore,
): TodayDataRepository {
    override suspend fun getTodayDate(): Flow<String> = todayDataStore.todayDateFlow
    override suspend fun updateTodayDate(date: String) = todayDataStore.writeTodayDate(date)

    override suspend fun getTodayStatus(): Flow<String> = todayDataStore.todayStatusFlow
    override suspend fun updateTodayStatus(status: String) = todayDataStore.writeTodayStatus(status)

    override suspend fun getTodayHistory(): Flow<String> = todayDataStore.todayHistoryFlow
    override suspend fun updateTodayHistory(history: String) = todayDataStore.writeTodayHistory(history)
}