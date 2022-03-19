package com.peopleofandroido.chillaxingcat.data.remote.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.model.DayInfo
import com.peopleofandroido.chillaxingcat.domain.model.DayInfoList
import java.util.*

data class HolidayResponse(
    @SerializedName("response")
    val response: Holiday
)

data class Holiday(
    @SerializedName("header")
    val header : JsonObject,
    @SerializedName("body")
    val body : Items,
)

data class Items (
    @SerializedName("items")
    val item : Item,
    @SerializedName("numOfRows")
    val numOfRows : Int,
    @SerializedName("pageNo")
    val pageNo : Int,
    @SerializedName("totalCount")
    val totalCount : Int,
)

data class Item (
    @SerializedName("item")
    val items : JsonArray,
)

internal fun Item.toDateModel(): List<DateModel> {
    val dateModelList : ArrayList<DateModel> = arrayListOf()

    for (jsonElement in items) {
        val itemInfo : DayInfo = Gson().fromJson(jsonElement, DayInfo::class.java)
        if (itemInfo.isHoliday == "Y") {
            dateModelList.add(DateModel(itemInfo.locdate, itemInfo.dateName))
            Log.e("GAEGUL", "item : ${itemInfo.dateName} / ${itemInfo.locdate} / ${itemInfo.isHoliday}")
        }
    }

    return dateModelList
}