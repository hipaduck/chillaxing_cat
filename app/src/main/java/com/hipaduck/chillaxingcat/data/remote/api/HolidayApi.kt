package com.hipaduck.chillaxingcat.data.remote.api

import com.hipaduck.chillaxingcat.data.remote.model.HolidayResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface HolidayApi {
    @GET("getRestDeInfo")
    suspend fun requestHoliday(
        @Query("solYear") year : String,
        @Query("solMonth") month : String,
        @Query("ServiceKey") authKey : String,
        @Query("_type") type : String = "json",
    ): Response<HolidayResponse>
}