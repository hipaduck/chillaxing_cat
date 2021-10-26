package com.peopleofandroido.base.di

import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.domain.ResultHandler
import org.koin.dsl.module

val componentModule = module {
    factory {
        ResultHandler()
    }

    single {
        NavManager()
    }
}