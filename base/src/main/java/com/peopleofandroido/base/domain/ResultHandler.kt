package com.peopleofandroido.base.domain

import retrofit2.HttpException
import java.net.SocketTimeoutException

open class ResultHandler {

    fun <T: Any> handleSuccess(data: T): Result<T> {
        return Result.success(data)
    }

    fun <T: Any> handleFailure(e: Exception): Result<T> {
        return when(e) {
            is HttpException -> Result.error(getErrorMessage(e.code()), null)
            is SocketTimeoutException -> Result.error(getErrorMessage(199), null)
            else -> {
                e.printStackTrace()
                Result.error(getErrorMessage(Int.MAX_VALUE), null)
            }

        }
    }

    fun <T: Any> handleFailure(msg: String): Result<T> = Result.error(msg, null)

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            401 -> "Unauthorized"
            404 -> "Not found"
            199 -> "Timeout" // 더미.
            else -> "Something went wrong"
        }
    }
}