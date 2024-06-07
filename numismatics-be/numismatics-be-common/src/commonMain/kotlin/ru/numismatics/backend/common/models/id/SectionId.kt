package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class SectionId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun toLong() = id.toLong()

    companion object {
        val EMPTY = SectionId(0U)
        fun from(id: Long?) = if (id == null) EMPTY else SectionId(id.toULong())
    }
}
