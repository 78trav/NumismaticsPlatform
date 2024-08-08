package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class LotId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun id() = id

    companion object {
        val EMPTY = LotId(EMPTY_ID)
    }
}

fun LotId.isEmpty(): Boolean = (this == LotId.EMPTY)

fun LotId.isNotEmpty(): Boolean = !isEmpty()

fun Long?.toLotId() = LotId(this?.toULong() ?: EMPTY_ID)
