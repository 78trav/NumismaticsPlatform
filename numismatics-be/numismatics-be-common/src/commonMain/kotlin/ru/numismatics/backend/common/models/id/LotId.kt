package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class LotId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun toLong() = id.toLong()

    companion object {
        val EMPTY = LotId(0U)
        fun from(id: Long?) = if (id == null) EMPTY else LotId(id.toULong())
    }
}
