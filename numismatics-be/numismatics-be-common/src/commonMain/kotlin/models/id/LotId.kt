package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class LotId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun toLong() = id.toLong()

    companion object {
        val EMPTY = LotId(0U)
    }
}

fun LotId.isEmpty(): Boolean = (this == LotId.EMPTY)

fun LotId.isNotEmpty(): Boolean = !isEmpty()

fun Long?.toLotId(): LotId = if (this == null) LotId.EMPTY else LotId(this.toULong())
