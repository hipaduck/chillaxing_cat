package com.peopleofandroido.base.di

import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val MAX_CACHE_SIZE = 20L * 1024 * 1024 // 20MB
private const val CONNECT_TIMEOUT = 15L // 15 seconds
private const val WRITE_TIMEOUT = 20L // 20 seconds
private const val READ_TIMEOUT = 20L // 20 seconds
private const val BASE_URL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/"

val networkModule = module {

    //Cache
    single {
        Cache(androidApplication().cacheDir, MAX_CACHE_SIZE)
    }

    //Gson
    single {
        GsonBuilder().setLenient().create()
    }

    //OkHttpClient
    single {
        OkHttpClient.Builder().apply {
            cache(get())
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(get() as HttpLoggingInterceptor)
            addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
        }.build()
    }

    //Retrofit
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get() as OkHttpClient)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    //HttpLoggingInterceptor
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}