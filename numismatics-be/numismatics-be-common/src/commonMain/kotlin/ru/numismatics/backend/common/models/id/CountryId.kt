package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class CountryId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun toLong() = id.toLong()

    companion object {
        val EMPTY = CountryId(0U)
        fun from(id: Long?) = if (id == null) EMPTY else CountryId(id.toULong())
    }
}
