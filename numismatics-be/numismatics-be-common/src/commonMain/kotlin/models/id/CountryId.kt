package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class CountryId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun id() = id

    companion object {
        val EMPTY = CountryId(EMPTY_ID)
    }
}

fun CountryId.isEmpty(): Boolean = (this == CountryId.EMPTY)

fun CountryId.isNotEmpty(): Boolean = !isEmpty()

fun Long?.toCountryId(): CountryId = CountryId(this?.toULong() ?: EMPTY_ID)
