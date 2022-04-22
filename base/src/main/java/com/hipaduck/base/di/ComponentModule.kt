package com.hipaduck.base.di

import com.hipaduck.base.common.NavManager
import com.hipaduck.base.domain.ResultHandler
import org.koin.dsl.module

val componentModule = module {
    factory {
        ResultHandler()
    }

    single {
        NavManager()
    }
}