package com.peopleofandroido.base.domain.model

data class ResultModel<T>(
    val code: Int, // 정상일 경우에는 0
    val message: String, // 에러메세지 포함
    val data: T?,
)