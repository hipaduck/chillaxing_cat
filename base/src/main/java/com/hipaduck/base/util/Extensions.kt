package com.hipaduck.base.util

import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.hipaduck.base.domain.model.ResultModel
import retrofit2.Response

fun Any.logd(msg: String) {
    Log.d("ChillaxingCat", "[${this::class.java.simpleName}] : $msg")
}

fun Any.logd(tag: String, msg: String) {
    Log.d("ChillaxingCat", "[$tag] : $msg")
}
fun Any.loge(msg: String, throwable: Throwable? = null) {
    throwable?.let {
        Log.e("ChillaxingCat", "[${this::class.java.simpleName}] : $msg", throwable)
    } ?: run {
        Log.e("ChillaxingCat", "[${this::class.java.simpleName}] : $msg")
    }
}

fun Any.loge(tag: String, msg: String, throwable: Throwable? = null) {
    throwable?.let {
        Log.e("ChillaxingCat", "[$tag] : $msg", throwable)
    } ?: run {
        Log.e("ChillaxingCat", "[$tag] : $msg")
    }
}

fun Fragment.showToast(msg: String) {
    return Toast.makeText(this.activity, msg, Toast.LENGTH_SHORT).show()
}

const val RESULT_CODE_UNKNOWN_ERROR = 910

fun <T> Response<*>.getErrorResultModel(gson: Gson): ResultModel<T> {
    val errorBody = this.errorBody()?.string() ?: "{}"
    return when (this.code()) {
        in 500 .. 599 -> {
            ResultModel(RESULT_CODE_UNKNOWN_ERROR, errorBody, null) // errorBody만 있는 상태로 반환
        }
        else -> {
            ResultModel(RESULT_CODE_UNKNOWN_ERROR, errorBody, null) // errorBody만 있는 상태로 반환
        }
    }
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()