package com.hipaduck.chillaxingcat.domain.model

data class RestTimeModel (
    var id   : Int,
    var history : String, // "{from timestamp}-{to timestamp}"
    var totalTime : Long // timestamp for during
)