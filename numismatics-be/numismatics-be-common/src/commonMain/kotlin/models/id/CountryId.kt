package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class CountryId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun toLong() = id.toLong()

    companion object {
        val EMPTY = CountryId(0U)
    }
}

fun CountryId.isEmpty(): Boolean = (this == CountryId.EMPTY)

fun CountryId.isNotEmpty(): Boolean = !isEmpty()

fun Long?.toCountryId(): CountryId = if (this == null) CountryId.EMPTY else CountryId(this.toULong())
