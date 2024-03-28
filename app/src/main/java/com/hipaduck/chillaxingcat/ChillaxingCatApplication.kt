package com.hipaduck.chillaxingcat

import android.app.Application
import com.hipaduck.base.di.componentModule
import com.hipaduck.base.di.networkModule
import com.hipaduck.chillaxingcat.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ChillaxingCatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger() //androidLogger를 KoinLogger로 사용
            androidContext(this@ChillaxingCatApplication) // 해당 안드로이드 context 사용
            koin.loadModules(arrayListOf( //이렇게로도 가능
                appViewModelModule,
                componentModule,
                repositoryModule,
                databaseModule,
                useCaseModule,
                networkModule,
                appDataStoreModule,
            ))
        }
    }
}