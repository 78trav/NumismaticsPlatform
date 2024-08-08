package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class SectionId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun id() = id

    companion object {
        val EMPTY = SectionId(EMPTY_ID)
    }
}

fun SectionId.isEmpty(): Boolean = (this == SectionId.EMPTY)

fun SectionId.isNotEmpty(): Boolean = !isEmpty()

fun Long?.toSectionId() = SectionId(this?.toULong() ?: EMPTY_ID)
