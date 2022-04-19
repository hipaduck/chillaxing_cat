package com.peopleofandroido.chillaxingcat.presentation.enums

import com.peopleofandroido.chillaxingcat.R

enum class TodayStatus(
    val type: String,
    val symbolImageDrawable: Int,
) {
    Normal("normal", R.drawable.cat_normal),
    Pause("pause", R.drawable.cat_work),
    Rest("rest", R.drawable.cat_home),
    Finish("finish", R.drawable.cat_sleep);

    companion object {
        fun typeOf(value: String) = when (value) {
            "normal" -> Normal
            "pause" -> Pause
            "rest" -> Rest
            "finish" -> Finish
            else -> Normal
        }
    }
}