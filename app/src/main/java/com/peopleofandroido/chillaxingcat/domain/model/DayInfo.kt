package com.peopleofandroido.chillaxingcat.domain.model

import com.google.gson.annotations.SerializedName

data class DayInfoList(
    val dayInfoList: List<DayInfo>
)

data class DayInfo (
    @SerializedName("dateKind")
    val dateKind : String,
    @SerializedName("dateName")
    val dateName : String,
    @SerializedName("isHoliday")
    val isHoliday : String,
    @SerializedName("locdate")
    val locdate : Int,
    @SerializedName("seq")
    val seq : Int,
)