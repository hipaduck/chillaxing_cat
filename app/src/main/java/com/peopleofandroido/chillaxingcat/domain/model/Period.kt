package com.peopleofandroido.chillaxingcat.domain.model

import java.text.DecimalFormat

data class Period(
    val year: Short,
    val month: Short,
    val day: Short,
    val yearStr: String = DecimalFormat("0000").format(year),
    val monthStr: String = DecimalFormat("00").format(month),
    val dayStr: String = DecimalFormat("00").format(day),
    val yearMonthId: Int = Integer.parseInt(yearStr + monthStr +dayStr)
)
