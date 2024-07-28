package ru.numismatics.backend.common.models.entities

import kotlinx.datetime.LocalDate

data class MarketPrice(
    val date: LocalDate,
    val amount: Float
)

fun String?.toLocalDateNP(): LocalDate? =
    this?.let {
        if (length == 8) {
            try {
                LocalDate.parse("${take(4)}-${take(6).takeLast(2)}-${takeLast(2)}")
            } catch (e: Exception) {
                null
            }
        } else
            null
    }

fun LocalDate.asString() =
    arrayOf(
        "${this.year}",
        "0${this.monthNumber}".takeLast(2),
        "0${this.dayOfMonth}".takeLast(2)
    ).joinToString("")