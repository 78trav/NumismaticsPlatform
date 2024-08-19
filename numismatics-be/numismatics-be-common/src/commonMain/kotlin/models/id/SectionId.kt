package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class SectionId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun toLong() = id.toLong()

    companion object {
        val EMPTY = SectionId(0U)
    }
}

fun SectionId.isEmpty(): Boolean = (this == SectionId.EMPTY)

fun SectionId.isNotEmpty(): Boolean = !isEmpty()

fun Long?.toSectionId(): SectionId = if (this == null) SectionId.EMPTY else SectionId(this.toULong())
