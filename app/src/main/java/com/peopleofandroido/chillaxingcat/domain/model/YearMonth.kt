package com.peopleofandroido.chillaxingcat.domain.model

import java.text.DecimalFormat

data class YearMonth(
    val year: Short,
    val month: Short,
    val yearStr: String = DecimalFormat("0000").format(year),
    val monthStr: String = DecimalFormat("00").format(month),
)
