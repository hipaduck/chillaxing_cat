package com.hipaduck.base.data

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class Item (
    @SerializedName("item")
    val items : JsonArray,
)