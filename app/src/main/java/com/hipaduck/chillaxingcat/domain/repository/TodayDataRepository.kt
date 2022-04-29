package com.hipaduck.chillaxingcat.domain.repository

import kotlinx.coroutines.flow.Flow

interface TodayDataRepository {
    suspend fun getTodayDate(): Flow<String>
    suspend fun updateTodayDate(date: String)

    suspend fun getTodayStatus(): Flow<String>
    suspend fun updateTodayStatus(status: String)

    suspend fun getTodayHistory(): Flow<String>
    suspend fun updateTodayHistory(history: String)
}